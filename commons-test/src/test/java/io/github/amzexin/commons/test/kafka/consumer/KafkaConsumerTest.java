package io.github.amzexin.commons.test.kafka.consumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import io.github.amzexin.commons.util.lang.SleepUtils;
import io.github.amzexin.commons.util.lang.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * KafkaConsumerTest
 *
 * @author zexin
 */
@Slf4j
public class KafkaConsumerTest extends BaseKafkaTest {

    @Test
    public void testAutoCommit() {
        Properties consumerProperties = getBaseConsumerProperties();
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerProperties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "200");
        KafkaConsumer<String, String> consumer = createConsumer(consumerProperties, null);
        SleepUtils.sleep(2000);
        log.info("consumer is ok");

        AtomicInteger recordCount = new AtomicInteger(0);
        AtomicLong startTime = new AtomicLong(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                    if (records.isEmpty()) {
                        continue;
                    }
                    if (startTime.get() == 0) {
                        startTime.set(System.currentTimeMillis());
                    }
                    for (ConsumerRecord<String, String> record : records) {
                        SleepUtils.sleep(200);
                        recordCount.incrementAndGet();
                    }
                }
            }
        }, "single thread").start();
        log.info("poll is ok");

        int lastPrintCount = 0;
        int curPrintCount = 0;
        while (true) {
            SleepUtils.sleep(100);
            if ((curPrintCount = recordCount.get()) != 0 && lastPrintCount != curPrintCount) {
                log.info("当前消费{}条, 耗时{}", recordCount.get(), TimeUtils.timeHumanize(System.currentTimeMillis() - startTime.get()));
                lastPrintCount = curPrintCount;
            }
        }

    }
}
