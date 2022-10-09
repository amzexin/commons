package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.MDC;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Description: ConcurrentConsumer
 *
 * @author Lizexin
 * @date 2022-09-29 16:51
 */
@Slf4j
public class ConcurrentConsumer {
    /**
     * 并发消费的数量
     */
    private final int concurrentCount;
    /**
     * 运行中的record
     * key: recordId
     * value: ConsumerRecordWrapper
     */
    private final Map<String, ConsumerRecordWrapper<String, String>> runningRecords;
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
     * 当前已提交的commit
     */
    private final Map<TopicPartition, Long> committedOffsetMap = new ConcurrentHashMap<>();
    /**
     * 当前可以提交的offset
     */
    private final Map<TopicPartition, Long> preCommitOffsetMap = new ConcurrentHashMap<>();
    /**
     * Record Consumer
     */
    private final Consumer<ConsumerRecord<String, String>> consumerRecordHandler;
    /**
     * kafka consumer client
     */
    private final KafkaConsumer<String, String> kafkaConsumer;
    /**
     * 当前状态
     */
    private final AtomicInteger state;
    private static final byte STARTED = 1;
    private static final byte STOPPING = 2;
    private static final byte STOPPED = 3;
    /**
     * shutdown时最大的等待时间
     */
    private final Duration shutdownTimeout;
    /**
     * 消费者的名字
     */
    private String name;

    public ConcurrentConsumer(KafkaConsumer<String, String> kafkaConsumer, int concurrentCount,
                              Consumer<ConsumerRecord<String, String>> consumerRecordHandler) {
        this(kafkaConsumer, concurrentCount, consumerRecordHandler, null);
    }

    public ConcurrentConsumer(KafkaConsumer<String, String> kafkaConsumer, int concurrentCount,
                              Consumer<ConsumerRecord<String, String>> consumerRecordHandler,
                              ThreadPoolExecutor threadPoolExecutor) {
        if (concurrentCount <= 0) {
            throw new RuntimeException("concurrentCount必须大于0");
        }
        this.kafkaConsumer = kafkaConsumer;
        this.concurrentCount = concurrentCount;
        this.runningRecords = new ConcurrentHashMap<>();
        this.concurrentConsumeSemaphore = new Semaphore(concurrentCount);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(
                    Math.min(Runtime.getRuntime().availableProcessors() * 2, concurrentCount),
                    Math.min(Runtime.getRuntime().availableProcessors() * 2, concurrentCount),
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()
            );
        }
        this.threadPoolExecutor = threadPoolExecutor;
        this.consumerRecordHandler = consumerRecordHandler;
        this.state = new AtomicInteger(STARTED);
        this.shutdownTimeout = Duration.ofSeconds(concurrentCount);
        this.name = UUID.randomUUID().toString().replaceAll("-", "").substring(16);
    }

    public boolean isStarted() {
        return state.get() == STARTED;
    }

    public boolean isStopping() {
        return state.get() == STOPPING;
    }

    public boolean isStopped() {
        return state.get() == STOPPED;
    }

    public void wakeup() {
        wakeup.set(true);
    }

    public void maybeTriggerWakeup() {
        if (wakeup.get()) {
            log.debug("ConcurrentConsumer[" + name + "]Raising WakeupException in response to user wakeup");
            wakeup.set(false);
            throw new ConcurrentConsumerWakeupException("ConcurrentConsumer[" + name + "]被Wakeup");
        }
    }

    /**
     * 异步消费
     *
     * @param record
     * @throws InterruptedException
     */
    public void consumeAsync(ConsumerRecord<String, String> record) throws InterruptedException {
        if (!isStarted()) {
            throw new RuntimeException("ConcurrentConsumer[" + name + "]已不在运行状态, 不允许继续消费");
        }
        // 为了尽可能地保证顺序, sleep 10ms
        TimeUnit.MILLISECONDS.sleep(10);

        while (true) {
            // 检查是否被唤醒
            maybeTriggerWakeup();
            // 争抢信号量
            if (concurrentConsumeSemaphore.tryAcquire(1, TimeUnit.SECONDS)) {
                break;
            }
        }
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ConsumerRecordWrapper<String, String> consumerRecordWrapper = new ConsumerRecordWrapper<>(record);
                try {
                    // 记录任务开始执行的信息
                    runningRecords.put(consumerRecordWrapper.getId(), consumerRecordWrapper);
                    // 执行真正的任务

                    consumerRecordHandler.accept(record);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    // 将需要提交的offset的添加到preCommitOffsetMap
                    long newPreCommitOffset = record.offset() + 1;
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    // 下面的操作是为了避免小的offset被提交
                    while (true) {
                        Long oldPreCommitOffset = preCommitOffsetMap.put(topicPartition, newPreCommitOffset);
                        if (oldPreCommitOffset == null || oldPreCommitOffset < newPreCommitOffset) {
                            break;
                        }
                        newPreCommitOffset = oldPreCommitOffset;
                    }
                    // 移除运行中任务的信息
                    runningRecords.remove(consumerRecordWrapper.getId());
                    // 释放信号量
                    concurrentConsumeSemaphore.release();
                    MDC.remove("trace_id");
                }
            }
        });
    }

    /**
     * 同步提交
     */
    public void commitSync() {
        if (preCommitOffsetMap.isEmpty()) {
            return;
        }
        Set<TopicPartition> topicPartitions = preCommitOffsetMap.keySet();
        for (TopicPartition topicPartition : topicPartitions) {
            Long preCommitOffset = preCommitOffsetMap.remove(topicPartition);
            if (preCommitOffset == null) {
                continue;
            }
            Long committedOffset = committedOffsetMap.get(topicPartition);
            if (committedOffset == null || preCommitOffset > committedOffset) {
                kafkaConsumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(preCommitOffset)));
                committedOffsetMap.put(topicPartition, preCommitOffset);
                log.debug("ConcurrentConsumer[{}] offset commit success, topicPartition = {}, preCommitOffset = {}", name, topicPartition, preCommitOffset);
            }
        }
    }

    /**
     * 优雅关闭
     */
    public void gracefulShutdown() {
        if (isStopped()) {
            return;
        }
        if (isStopping()) {
            throw new RuntimeException("ConcurrentConsumer[" + name + "]正在关闭中");
        }
        if (!state.compareAndSet(STARTED, STOPPING)) {
            throw new RuntimeException("ConcurrentConsumer[" + name + "]和预期状态不符");
        }

        log.info("ConcurrentConsumer[{}]开始关闭", name);
        // 关闭之前, 先将任务执行完。shutdownTimeout即为超时时间。关闭期间每隔1s打印一下线程池运行情况
        long waitSeconds = shutdownTimeout.getSeconds();
        int availablePermits;
        while ((availablePermits = concurrentConsumeSemaphore.availablePermits()) != concurrentCount && waitSeconds > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn("ConcurrentConsumer[{}]优雅关闭过程中, 主线程触发interrupt, 忽略它继续优雅关闭", name);
            }
            waitSeconds--;
            int activeCount = threadPoolExecutor.getActiveCount();
            BlockingQueue<Runnable> blockingQueue = threadPoolExecutor.getQueue();
            log.info("ConcurrentConsumer[{}]关闭中, 倒数: {}, 线程池: (活跃线程数: {}, 等待队列中任务数: {})", name, waitSeconds, activeCount, blockingQueue.size());
        }

        try {
            commitSync();
        } catch (WakeupException e) {
            log.warn("{}, 没有将KafkaConsumer中wakeup标志位重置, 重新提交一下", name);
            commitSync();
        }

        state.compareAndSet(STOPPING, STOPPED);
        if (availablePermits != concurrentCount) {
            // 项目在关闭的时候会尽可能的做到优雅关闭, 等待了这么久都没有处理完, 业务逻辑必定存在某些问题
            log.error("ConcurrentConsumer[{}]超时{}关闭 偶买噶！！！！！！！！！！！！ 这意味着消息有可能被重复消费, 也有可能某个消息被强制中断且再也不会被执行.", name, shutdownTimeout);
            log.warn("ConcurrentConsumer[{}]没有执行完的任务有{}({})个, 请重点关注他们, 到目前为止, 他们执行耗时情况如下", name
                    , concurrentCount - availablePermits
                    , concurrentCount - concurrentConsumeSemaphore.availablePermits());
            runningRecords.forEach((recordId, consumerRecordWrapper) ->
                    log.warn("recordId = {}, handleTime = {}ms, recordValue = {}", recordId, System.currentTimeMillis() - consumerRecordWrapper.getExecuteStartTimestamp(), consumerRecordWrapper.recordValue())
            );
            return;
        }

        log.info("ConcurrentConsumer[{}]正常关闭 噢耶(^o^)", name);
    }
}
