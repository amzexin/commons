package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.WakeupException;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Description: ConcurrentConsumeTest
 *
 * @author Lizexin
 * @date 2022-09-28 14:54
 */
@Slf4j
public class ConcurrentConsumerTest extends BaseKafkaTest {

    private KafkaConsumer<String, String> kafkaConsumer;

    private Thread thread;


    @Before
    public void before() {
        int concurrentCount = 10;

        Properties consumerProperties = getBaseConsumerProperties();
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, concurrentCount + "");

        kafkaConsumer = createConsumer(consumerProperties);
        ConcurrentConsumer concurrentConsumer = new ConcurrentConsumer(kafkaConsumer, concurrentCount, new Consumer() {
            @Override
            public void accept(Object o) {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                int millis = (new Random().nextInt(10) + 10) * 1000;
                log.info("message handle start: topic, partition, offset = [{}, {}, {}] sleep = {}, value = {}", topic, partition, offset, millis, record.value());
                // 模拟消息处理
                SleepUtils.sleep(millis);
                log.info("message handle end: topic, partition, offset = [{}, {}, {}] value = {}", topic, partition, offset, record.value());
            }
        });
        SleepUtils.sleep(2000);
        log.info("consumer is ok");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        TraceIdUtils.setupTraceId();
                        concurrentConsumer.commitSync();
                        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
                        if (records.isEmpty()) {
                            continue;
                        }

                        int count = records.count();
                        if (count > 1) {
                            log.info("records size: {}", count);
                        }

                        for (ConsumerRecord<String, String> record : records) {
                            concurrentConsumer.consumeAsync(record);
                        }

                    } catch (WakeupException e) {
                        log.warn("主线程触发wakeup, 不再消费", e);
                        break;
                    } catch (InterruptedException | InterruptException e) {
                        log.warn("主线程触发{}, 不再消费", e.getClass().getName(), e);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
                concurrentConsumer.gracefulShutdown();
                kafkaConsumer.close();
            }
        }, "main");
        thread.start();
    }

    /**
     * 并发消费
     */
    @Test
    public void testConcurrentConsume() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("是否停止(y/n): ");
            String str = scanner.nextLine();
            if ("y".equalsIgnoreCase(str)) {
                break;
            }
        }
        kafkaConsumer.wakeup();
        thread.join(TimeUnit.SECONDS.toMillis(30));
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>over");
    }


}
