package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Description: ConcurrentKafkaConsumer
 *
 * @author Lizexin
 * @date 2022-09-29 16:51
 */
@Slf4j
public class ConcurrentKafkaConsumer {
    /**
     * 并发消费的数量
     */
    private final int INFLIGHT_WINDOW_SIZE;
    /**
     * 运行中的record
     * key: recordId
     * value: ConsumerRecordWrapper
     */
    private final Map<String, ConsumerRecordWrapper<String, String>> runningRecords = new ConcurrentHashMap<>();
    /**
     * 5s内已处理的record
     * 为了避免因rebalance导致的重复消费
     */
    private final Set<String> consumedRecordIds = new ConcurrentSkipListSet<>();
    /**
     * 异步周期执行的线程池
     * 用于定期将已完成的record从consumedRecordIds移除
     */
    private final ScheduledExecutorService scheduledThreadPoolExecutor = Executors.newSingleThreadScheduledExecutor();
    /**
     * 控制并发消费的信号量
     */
    private final Semaphore concurrentConsumeSemaphore;
    /**
     * 这个标志允许客户端被安全地唤醒, 而不需要等待信号量的锁。
     */
    private final AtomicBoolean wakeup = new AtomicBoolean(false);
    /**
     * 并发消费的线程池
     */
    private final ThreadPoolExecutor threadPoolExecutor;
    /**
     * 当前可以提交的offset
     */
    private final Map<TopicPartition, Long> preCommitOffsetMap = new ConcurrentHashMap<>();
    /**
     * 当前已提交的commit
     */
    private final Map<TopicPartition, Long> committedOffsetMap = new ConcurrentHashMap<>();
    /**
     * 待提交的record数
     */
    private final AtomicInteger preCommitRecordCount = new AtomicInteger(0);
    /**
     * Record Consumer
     */
    private final Map<String, Consumer<ConsumerRecord<String, String>>> recordConsumerMap = new ConcurrentHashMap<>();
    /**
     * kafka consumer client
     */
    private final KafkaConsumer<String, String> kafkaConsumer;
    /**
     * 当前状态。正常的状态变化有如下两种:
     * 1. NEW -> STARTED -> CLOSING -> CLOSED
     * 2. NEW -> CLOSING -> CLOSED
     */
    private final AtomicInteger state;
    private static final byte NEW = 0;
    private static final byte STARTED = 1;
    private static final byte CLOSING = 2;
    private static final byte CLOSED = 3;
    /**
     * shutdown时最大的等待时间
     */
    private final Duration shutdownTimeout;
    /**
     * 消费者的名字
     */
    private String name;
    /**
     * 包装名称
     */
    private String wrapName;

    public ConcurrentKafkaConsumer(Properties properties) {
        this(properties, null);
    }

    public ConcurrentKafkaConsumer(Properties properties, ThreadPoolExecutor threadPoolExecutor) {
        // 必须为手动提交
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 最大拉取记录数设置为1. 消费者总是批量获取并保存在内存中, 只是每次poll时, 返回的记录数最多为max.poll.records个
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        this.INFLIGHT_WINDOW_SIZE = Runtime.getRuntime().availableProcessors();
        this.kafkaConsumer = new KafkaConsumer<>(properties);
        this.concurrentConsumeSemaphore = new Semaphore(INFLIGHT_WINDOW_SIZE);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(INFLIGHT_WINDOW_SIZE, INFLIGHT_WINDOW_SIZE,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()
            );
        }
        this.threadPoolExecutor = threadPoolExecutor;
        this.state = new AtomicInteger(NEW);
        this.shutdownTimeout = Duration.ofSeconds(INFLIGHT_WINDOW_SIZE * 2L);
        this.name = UUID.randomUUID().toString().replaceAll("-", "").substring(16);
        this.wrapName = "ConcurrentKafkaConsumer[" + name + "]";
    }

    public boolean isNew() {
        return state.get() == NEW;
    }

    public boolean isStarted() {
        return state.get() == STARTED;
    }

    public boolean isStopping() {
        return state.get() == CLOSING;
    }

    public boolean isClosed() {
        return state.get() == CLOSED;
    }

    public void wakeup() {
        wakeup.set(true);
    }

    public void maybeTriggerWakeup() {
        if (wakeup.get()) {
            log.debug("{} Raising WakeupException in response to user wakeup", wrapName);
            wakeup.set(false);
            throw new CkcWakeupException(wrapName + "被Wakeup");
        }
    }

    /**
     * 异步消费
     *
     * @param record
     * @throws InterruptedException
     */
    private void consumeAsync(ConsumerRecord<String, String> record) throws InterruptedException {
        do {
            if (!isStarted()) {
                throw new CkcNotStartedException(wrapName + "已不在运行状态, 不允许继续消费");
            }
            // 检查是否被唤醒
            maybeTriggerWakeup();
            // 争抢信号量
        } while (!concurrentConsumeSemaphore.tryAcquire(1, TimeUnit.SECONDS));

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ConsumerRecordWrapper<String, String> consumerRecordWrapper = new ConsumerRecordWrapper<>(record);
                String recordId = consumerRecordWrapper.getId();

                // 检测该任务在当前Consumer是否已经执行过, 包括正在执行的
                if (runningRecords.containsKey(recordId) || consumedRecordIds.contains(recordId)) {
                    concurrentConsumeSemaphore.release();
                    return;
                }

                try {
                    // 记录任务开始执行的信息
                    runningRecords.put(recordId, consumerRecordWrapper);
                    // 执行真正的任务
                    recordConsumerMap.get(record.topic()).accept(record);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    // 将需要提交的offset的添加到preCommitOffsetMap
                    final long newPreCommitOffset = record.offset() + 1;
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    preCommitOffsetMap.compute(topicPartition, (topicPartition1, oldPreCommitOffset) ->
                            oldPreCommitOffset == null || newPreCommitOffset > oldPreCommitOffset ? newPreCommitOffset : oldPreCommitOffset
                    );
                    // 待提交的记录数+1
                    preCommitRecordCount.incrementAndGet();
                    // 添加到已消费的记录中
                    consumedRecordIds.add(recordId);
                    // 从运行中记录中移除
                    runningRecords.remove(recordId);
                    // 释放信号量
                    concurrentConsumeSemaphore.release();
                    MDC.remove("trace_id");
                    scheduledThreadPoolExecutor.schedule(new Runnable() {
                        @Override
                        public void run() {
                            consumedRecordIds.remove(recordId);
                        }
                    }, 5000 + System.currentTimeMillis() - consumerRecordWrapper.getExecuteStartTimestamp(), TimeUnit.MILLISECONDS);
                }
            }
        });
    }

    /**
     * 同步提交
     */
    private void commitSync() {
        if (this.preCommitOffsetMap.isEmpty()) {
            return;
        }
        synchronized (this.kafkaConsumer) {
            // 将待提交的offset提交上去
            Set<TopicPartition> topicPartitions = this.preCommitOffsetMap.keySet();
            for (TopicPartition topicPartition : topicPartitions) {
                this.preCommitOffsetMap.compute(topicPartition, (key, preCommitOffset) -> {
                    // 实际应该不会为null, 但为了健壮考虑加了此判断
                    if (preCommitOffset == null) {
                        return null;
                    }

                    // 提交offset操作
                    Long committedOffset = this.committedOffsetMap.get(key);
                    if (committedOffset == null || preCommitOffset > committedOffset) {
                        this.kafkaConsumer.commitSync(Collections.singletonMap(key, new OffsetAndMetadata(preCommitOffset)));
                        this.committedOffsetMap.put(key, preCommitOffset);
                        log.debug("{} offset commit success, topicPartition = {}, preCommitOffset = {}", this.wrapName, key, preCommitOffset);
                        // 返回null表示从preCommitOffsetMap移除该项, 到这一块说明真正的提交成功了, preCommitOffsetMap不需要保留此项
                        return null;
                    }

                    // 说明待提交的offset小于已经提交的offset, 返回null后, preCommitOffsetMap会删除该项
                    return null;
                });
            }

            // 重新设置未提交的记录数
            this.preCommitRecordCount.set(this.preCommitOffsetMap.size());
        }
    }

    /**
     * 订阅topic
     */
    public void subscribe(Collection<String> topics, Consumer<ConsumerRecord<String, String>> recordConsumer) {
        if (recordConsumer == null) {
            throw new RuntimeException("recordConsumer不允许为空");
        }
        if (isStopping()) {
            throw new RuntimeException(wrapName + "正在停止中, 不允许订阅topic");
        }
        if (isClosed()) {
            throw new RuntimeException(wrapName + "已停止, 不允许订阅topic");
        }

        // 过滤已经订阅的topic
        List<String> needSubscribeTopics = topics.stream().filter(topic -> {
            // 如果topic已订阅过，就仅替换recordConsumer
            Consumer<ConsumerRecord<String, String>> oldValue = recordConsumerMap.put(topic, recordConsumer);
            return oldValue == null;
        }).collect(Collectors.toList());

        if (needSubscribeTopics.isEmpty()) {
            return;
        }

        log.info("实际订阅的Topic有: {}", needSubscribeTopics);
        synchronized (this.kafkaConsumer) {
            this.kafkaConsumer.subscribe(needSubscribeTopics, new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    log.info("onPartitionsRevoked");
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    log.info("onPartitionsAssigned");
                }
            });
            log.info("订阅ok");
        }
    }

    /**
     * 异步poll并消费消息
     */
    public void pollAsync() {
        if (isStarted()) {
            throw new RuntimeException(wrapName + "已在运行中, 不允许重复开启消费");
        }
        if (isStopping()) {
            throw new RuntimeException(wrapName + "正在停止中, 不允许开启消费");
        }
        if (isClosed()) {
            throw new RuntimeException(wrapName + "已停止, 不允许开启消费");
        }
        if (!state.compareAndSet(NEW, STARTED)) {
            throw new RuntimeException(wrapName + "当前状态[" + state.get() + "]无法开启消费");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStarted()) {
                    try {
                        TraceIdUtils.setupTraceId();

                        ConsumerRecords<String, String> records;
                        synchronized (kafkaConsumer) {
                            records = kafkaConsumer.poll(Duration.ofSeconds(INFLIGHT_WINDOW_SIZE));
                        }

                        // records为空有两种可能: 1. 确实没有消息; 2. 当前正在rebalance, 所以这个时候要立即提交待提交的offset, 尽可能的减少重复消费
                        if (records == null || records.isEmpty()) {
                            commitSync();
                            continue;
                        }

                        for (ConsumerRecord<String, String> record : records) {
                            consumeAsync(record);
                        }

                        // 待提交的记录数达到一个窗口的大小之后, 触发一次提交
                        if (preCommitRecordCount.get() >= INFLIGHT_WINDOW_SIZE) {
                            commitSync();
                        }
                    } catch (WakeupException | CkcWakeupException e) {
                        log.warn("{}主线程触发{}", wrapName, e.getClass().getName());
                    } catch (CkcNotStartedException e) {
                        break;
                    } catch (Exception e) {
                        log.error("{}出现异常: {}", wrapName, e.getMessage(), e);
                    }
                }
                log.warn("{}不再poll消息, 请知悉", wrapName);
            }
        }, wrapName).start();
    }

    /**
     * 优雅关闭
     */
    public void close() {
        if (isClosed()) {
            return;
        }
        if (isStopping()) {
            throw new RuntimeException(wrapName + "正在关闭中, 不允许重复关闭");
        }
        if (!state.compareAndSet(STARTED, CLOSING) && !state.compareAndSet(NEW, CLOSING)) {
            throw new RuntimeException(wrapName + "当前状态[" + state.get() + "]无法关闭");
        }

        log.info("{}开始关闭", wrapName);
        kafkaConsumer.wakeup();
        wakeup();
        // 关闭之前, 先将任务执行完。shutdownTimeout即为超时时间。关闭期间每隔1s打印一下线程池运行情况
        long waitSeconds = shutdownTimeout.getSeconds();
        int availablePermits;
        while ((availablePermits = concurrentConsumeSemaphore.availablePermits()) != INFLIGHT_WINDOW_SIZE && waitSeconds > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn("{}优雅关闭过程中, 主线程触发interrupt, 忽略它继续优雅关闭", wrapName);
            }
            waitSeconds--;
            int activeCount = threadPoolExecutor.getActiveCount();
            BlockingQueue<Runnable> blockingQueue = threadPoolExecutor.getQueue();
            log.info("{}关闭中, 倒数: {}, 信号量: {}({}), 线程池: (活跃线程数: {}, 等待队列中任务数: {})",
                    wrapName, waitSeconds, availablePermits, INFLIGHT_WINDOW_SIZE, activeCount, blockingQueue.size());
        }

        try {
            commitSync();
        } catch (WakeupException e) {
            log.warn("{}没有将KafkaConsumer中wakeup标志位重置, 重新提交一下", wrapName);
            commitSync();
        }

        log.info("{}开始关闭KafkaConsumer", wrapName);
        synchronized (this.kafkaConsumer) {
            this.kafkaConsumer.close();
        }

        state.compareAndSet(CLOSING, CLOSED);
        if (availablePermits != INFLIGHT_WINDOW_SIZE) {
            // 项目在关闭的时候会尽可能的做到优雅关闭, 等待了这么久都没有处理完, 业务逻辑必定存在某些问题
            log.error("{}超时{}关闭 偶买噶！！！！！！！！！！！！ 这意味着消息有可能被重复消费, 也有可能某个消息被强制中断且再也不会被执行.", wrapName, shutdownTimeout);
            log.warn("{}没有执行完的任务有{}个, 请重点关注他们, 到目前为止, 他们执行耗时情况如下", wrapName
                    , INFLIGHT_WINDOW_SIZE - availablePermits);
            runningRecords.forEach((recordId, consumerRecordWrapper) ->
                    log.warn("recordId = {}, handleTime = {}ms, recordValue = {}", recordId, System.currentTimeMillis() - consumerRecordWrapper.getExecuteStartTimestamp(), consumerRecordWrapper.recordValue())
            );
        } else {
            log.info("{}正常关闭 噢耶(^o^)", wrapName);
        }
    }
}
