package io.github.amzexin.commons.test.kafka.consumer;

import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * KafkaConsumerTest
 *
 * @author zexin
 */
@Slf4j
public class OffsetTest extends BaseKafkaTest {

    @Test
    public void testSeekToEnd() throws InterruptedException, IOException {
        Properties consumerProperties = getBaseConsumerProperties();
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerProperties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "200");
        KafkaConsumer<String, String> consumer = createConsumer(consumerProperties, null);
        log.info("consumer is ok");

        for (int i = 0; i < 3; i++) {

            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(5));
            log.info("拉取到{}条消息", consumerRecords.count());

            if (consumerRecords.isEmpty()) {
                continue;
            }

            log.info("seek to end -- start");
            consumer.seekToEnd(Collections.emptyList());
            log.info("seek to end -- end");
        }

        log.info("all is ok");


    }
}
