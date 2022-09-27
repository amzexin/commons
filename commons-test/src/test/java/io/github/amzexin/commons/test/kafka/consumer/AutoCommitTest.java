package io.github.amzexin.commons.test.kafka.consumer;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.util.io.FileUtils;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: AutoCommitTest
 *
 * @author Lizexin
 * @date 2022-09-27 14:12
 */
@Slf4j
public class AutoCommitTest {

    private static final String consumerPropertiesKeyPrefix = "consumer.";

    private static final String consumerSubscribeTopicsKey = "consumer.subscribe.topics";

    private Properties consumerProperties;

    private List<String> consumerSubscribeTopics;

    /**
     * 加载基础配置
     *
     * @throws IOException
     */
    @Before
    public void before() throws IOException {
        // 加载本地配置文件
        String propertiesPath = "/Users/lizexin/amzexin/xxx/commons-config/kafka.properties";
        Properties properties = FileUtils.getProperties(propertiesPath);

        // 获取consumer相关配置
        this.consumerProperties = new Properties();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            if (consumerSubscribeTopicsKey.equalsIgnoreCase(key)) {
                String consumerSubscribeTopicsValue = properties.getProperty(key);
                consumerSubscribeTopics = Arrays.stream(consumerSubscribeTopicsValue.split(",")).map(String::trim).collect(Collectors.toList());
            } else if (key.startsWith(consumerPropertiesKeyPrefix)) {
                this.consumerProperties.setProperty(key.substring(consumerPropertiesKeyPrefix.length()), properties.getProperty(key));
            }
        }

        // 如果没有配置topic时，初始化一个空topicList
        if (this.consumerSubscribeTopics == null) {
            this.consumerSubscribeTopics = new ArrayList<>();
        }
    }

    private KafkaConsumer<String, String> createConsumer() {
        // 创建consumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(this.consumerProperties);

        // 订阅消息
        if (!consumerSubscribeTopics.isEmpty()) {
            kafkaConsumer.subscribe(consumerSubscribeTopics);
        }

        return kafkaConsumer;
    }

    /**
     * case说明: 当enable.auto.commit=false时, 如果没有提交某条记录, 能否拉取到新的消息
     */
    @Test
    public void manualCommit() throws IOException {
        consumerProperties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer<String, String> consumer = createConsumer();
        SleepUtils.sleep(2000);
        log.info("consumer is ok");

        int foreachNum = 0;
        while (true) {
            TraceIdUtils.setupTraceId("for " + (++foreachNum));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
            if (!records.isEmpty()) {
                int count = records.count();
                if (count > 1) {
                    log.info("records size: {}", count);
                }
            }
            int recordIndex = 0;
            for (ConsumerRecord<String, String> record : records) {
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                if (recordIndex % 2 != 0) {
                    TopicPartition topicPartition = new TopicPartition(topic, partition);
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset + 1);
                    consumer.commitSync(Collections.singletonMap(topicPartition, offsetAndMetadata));
                }
                log.info("recordIndex = {}, partition = {}_{}, offset = {}, value = {}", recordIndex, topic, partition, offset, record.value());
                recordIndex++;
            }
            consumer.commitSync();
        }

    }


}
