package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
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
     * 控制并发消费的信号量
     */
    private final Semaphore concurrentConsumeSemaphore;
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
    private final Duration shutdownTimeout = Duration.ofSeconds(10);

    public ConcurrentConsumer(KafkaConsumer<String, String> kafkaConsumer,
                              int concurrentCount,
                              Consumer<ConsumerRecord<String, String>> consumerRecordHandler) {
        if (concurrentCount <= 0) {
            throw new RuntimeException("concurrentCount必须大于0");
        }
        this.kafkaConsumer = kafkaConsumer;
        this.concurrentCount = concurrentCount;
        this.concurrentConsumeSemaphore = new Semaphore(concurrentCount);
        int nThreads = Runtime.getRuntime().availableProcessors() * 2;
        this.threadPoolExecutor = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.consumerRecordHandler = consumerRecordHandler;
        this.state = new AtomicInteger(STARTED);
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

    /**
     * 异步消费
     *
     * @param record
     * @throws InterruptedException
     */
    public void consumeAsync(ConsumerRecord<String, String> record) throws InterruptedException {
        if (!isStarted()) {
            throw new RuntimeException("已不在运行状态，不允许继续消费");
        }
        // 为了尽可能地保证顺序
        TimeUnit.MILLISECONDS.sleep(10);
        concurrentConsumeSemaphore.acquire(); // 如果主线程触发停止，但是始终获取不到信号量，就会卡在这里，不太好
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TraceIdUtils.setupTraceId();
                    consumerRecordHandler.accept(record);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    preCommitOffsetMap.put(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
                    concurrentConsumeSemaphore.release();
                    TraceIdUtils.clearTraceId();
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
                log.info("offset commit success, topicPartition = {}, preCommitOffset = {}", topicPartition, preCommitOffset);
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
            throw new RuntimeException("正在关闭中");
        }
        if (!state.compareAndSet(STARTED, STOPPING)) {
            throw new RuntimeException("和预期状态不符");
        }

        log.info("ConcurrentConsumer开始关闭");
        // 关闭之前，先将任务执行完。shutdownTimeout即为超时时间。关闭期间每隔1s打印一下线程池运行情况
        long waitSeconds = shutdownTimeout.getSeconds();
        int availablePermits;
        while ((availablePermits = concurrentConsumeSemaphore.availablePermits()) != concurrentCount && waitSeconds > 0) {
            int activeCount = threadPoolExecutor.getActiveCount();
            BlockingQueue<Runnable> blockingQueue = threadPoolExecutor.getQueue();
            log.info("ConcurrentConsumer关闭中, 倒数: {}, 线程池: (活跃线程数: {}, 等待队列任务数: {})", waitSeconds, activeCount, blockingQueue.size());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn("关闭过程中，主线程发生interrupt");
            }
            waitSeconds--;
        }

        commitSync();
        state.compareAndSet(STOPPING, STOPPED);
        if (availablePermits == concurrentCount) {
            log.info("ConcurrentConsumer正常关闭 噢耶(^o^)");
        } else {
            log.error("ConcurrentConsumer超时关闭 偶买噶！！！！！！！！！！！！");
        }
    }
}
