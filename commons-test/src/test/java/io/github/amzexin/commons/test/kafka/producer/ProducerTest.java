package io.github.amzexin.commons.test.kafka.producer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: ProducerTest
 *
 * @author Lizexin
 * @date 2022-09-28 11:00
 */
@Slf4j
public class ProducerTest extends BaseKafkaTest {

    @Test
    public void testPublish() throws IOException {
        Properties producerProperties = getBaseProducerProperties();
        KafkaProducer<String, String> kafkaProducer = createProducer(producerProperties);

        String producerSendTopicsValue = producerProperties.getProperty(BaseKafkaTest.producerSendTopicsKey);
        List<String> producerSendTopics = Arrays.stream(producerSendTopicsValue.split(",")).map(String::trim).collect(Collectors.toList());
        if (producerSendTopics.isEmpty()) {
            return;
        }

        int num = 0;
        int msgId = 0;
        while (true) {
            for (String producerSendTopic : producerSendTopics) {
                String traceId = UUID.randomUUID().toString().replaceAll("-", "");
                String msg = String.format("%s - %d", traceId, msgId++);
                // kafkaProducer.send(new ProducerRecord<>(producerSendTopic, msg), (recordMetadata, e) -> {
                kafkaProducer.send(new ProducerRecord<>(producerSendTopic, 0, null, msg), (recordMetadata, e) -> {
                    TraceIdUtils.setupTraceId(traceId);
                    if (e != null) {
                        log.error("kafka send error: {}", e.getMessage(), e);
                    } else {
                        log.info("kafka send success");
                    }
                });
            }
            if (num <= 1) {
                System.out.print("请输入要发送的消息数: ");
                Scanner scanner = new Scanner(System.in);
                num = scanner.nextInt();
                num = Math.max(num, 1);
            } else {
                num--;
            }
        }

    }
}
