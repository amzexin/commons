package io.github.amzexin.commons.test.kafka.consumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Description: ConcurrentConsumeTest
 *
 * @author Lizexin
 * @date 2022-09-28 14:54
 */
@Slf4j
public class ConcurrentConsumeTest extends BaseKafkaTest {

    private static class ConcurrentConsumer {
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
        private final ExecutorService executorService;
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
        private final Consumer<ConsumerRecord<String, String>> consumerRecordConsumer;
        /**
         * kafka consumer client
         */
        private final KafkaConsumer<String, String> kafkaConsumer;

        public ConcurrentConsumer(KafkaConsumer<String, String> kafkaConsumer, int concurrentCount, Consumer<ConsumerRecord<String, String>> consumerRecordConsumer) {
            if (concurrentCount <= 0) {
                throw new RuntimeException("concurrentCount必须大于0");
            }
            this.kafkaConsumer = kafkaConsumer;
            this.concurrentCount = concurrentCount;
            this.concurrentConsumeSemaphore = new Semaphore(concurrentCount);
            this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
            this.consumerRecordConsumer = consumerRecordConsumer;
        }

        public int getConcurrentCount() {
            return concurrentCount;
        }

        public void consume(ConsumerRecord<String, String> record) throws InterruptedException {
            // 为了尽可能地保证顺序
            TimeUnit.MILLISECONDS.sleep(10);
            concurrentConsumeSemaphore.acquire();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TraceIdUtils.setupTraceId();
                        consumerRecordConsumer.accept(record);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        canCommit(record);
                        concurrentConsumeSemaphore.release();
                        TraceIdUtils.clearTraceId();
                    }
                }
            });
        }

        private void canCommit(ConsumerRecord<String, String> record) {
            TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
            preCommitOffsetMap.put(topicPartition, record.offset() + 1);
        }

        public void commitOffset() {
            if (preCommitOffsetMap.isEmpty()) {
                return;
            }
            try {
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
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 并发消费
     */
    @Test
    public void testConcurrentConsume() throws InterruptedException {

        int concurrentCount = 10;

        Properties consumerProperties = getBaseConsumerProperties();
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, concurrentCount + "");

        KafkaConsumer<String, String> kafkaConsumer = createConsumer(consumerProperties);
        ConcurrentConsumer concurrentConsumer = new ConcurrentConsumer(kafkaConsumer, concurrentCount, new Consumer() {
            @Override
            public void accept(Object o) {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                int millis = new Random().nextInt(10) * 1000;
                log.info("message handle start: topic, partition, offset = [{}, {}, {}] sleep = {}, value = {}", topic, partition, offset, millis, record.value());
                SleepUtils.sleep(millis);
                log.info("message handle end: topic, partition, offset = [{}, {}, {}] value = {}", topic, partition, offset, record.value());
            }
        });
        SleepUtils.sleep(2000);
        log.info("consumer is ok");

        while (true) {

            TraceIdUtils.setupTraceId();
            concurrentConsumer.commitOffset();
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
            if (records.isEmpty()) {
                continue;
            }

            int count = records.count();
            if (count > 1) {
                log.info("records size: {}", count);
            }

            for (ConsumerRecord<String, String> record : records) {
                concurrentConsumer.consume(record);
            }
        }

    }


}
