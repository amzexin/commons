package io.github.amzexin.commons.test.kafka.consumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

/**
 * Description: AutoCommitTest
 *
 * @author Lizexin
 * @date 2022-09-27 14:12
 */
@Slf4j
public class AutoCommitTest extends BaseKafkaTest {

    /**
     * case说明: 当enable.auto.commit=false时, 如果没有提交某条记录, 能否拉取到新的消息
     * 结果说明: 可以拉取到最新的消息
     */
    @Test
    public void manualCommit() {
        Properties consumerProperties = getBaseConsumerProperties();
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer<String, String> consumer = createConsumer(consumerProperties);
        SleepUtils.sleep(2000);
        log.info("consumer is ok");

        int foreachNum = 0;
        while (true) {
            TraceIdUtils.setupTraceId("for " + (++foreachNum));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
            if (records.isEmpty()) {
                continue;
            }

            int count = records.count();
            if (count > 1) {
                log.info("records size: {}", count);
            }

            for (ConsumerRecord<String, String> record : records) {
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                log.info("topic, partition, offset = [{}, {}, {}], value = {}", topic, partition, offset, record.value());
            }

            System.out.print("请输入要提交的topic, partition, offset, 逗号隔开: ");
            Scanner scanner = new Scanner(System.in);
            String newOffsetStr = scanner.nextLine();
            if (newOffsetStr == null || newOffsetStr.isEmpty()) {
                // 提交当前offset
                consumer.commitSync();
            } else if ("n".equalsIgnoreCase(newOffsetStr)) {
                // 不提交
            } else {
                String[] split = newOffsetStr.split(",");
                String topic = split[0].trim();
                int partition = Integer.parseInt(split[1].trim());
                int newOffset = Integer.parseInt(split[2].trim());
                TopicPartition topicPartition = new TopicPartition(topic, partition);
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(newOffset);
                consumer.commitSync(Collections.singletonMap(topicPartition, offsetAndMetadata));
            }
            System.out.println("------");
        }

    }


}
