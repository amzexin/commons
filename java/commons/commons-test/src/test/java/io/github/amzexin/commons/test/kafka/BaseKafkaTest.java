package io.github.amzexin.commons.test.kafka;

import io.github.amzexin.commons.util.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.TopicPartition;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: BaseKafkaTest
 *
 * @author Lizexin
 * @date 2022-09-28 11:01
 */
public class BaseKafkaTest {

    public static final String consumerPropertiesKeyPrefix = "consumer.";

    public static final String consumerSubscribeTopicsKey = "my.subscribe.topics";

    public static final String producerPropertiesKeyPrefix = "producer.";

    public static final String producerSendTopicsKey = "my.send.topics";

    private final Properties consumerProperties = new Properties();

    private final Properties producerProperties = new Properties();

    public BaseKafkaTest() {
        // 加载本地配置文件
        String propertiesPath = "/Users/lizexin/amzexin/xxx/commons-config/kafka.properties";
        Properties properties = null;

        try {
            properties = FileUtils.getProperties(propertiesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 获取相关配置
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith(consumerPropertiesKeyPrefix)) {
                this.consumerProperties.setProperty(propertyName.substring(consumerPropertiesKeyPrefix.length())
                        , properties.getProperty(propertyName));
            } else if (propertyName.startsWith(producerPropertiesKeyPrefix)) {
                this.producerProperties.setProperty(propertyName.substring(producerPropertiesKeyPrefix.length())
                        , properties.getProperty(propertyName));
            }
        }

    }

    public Properties getBaseConsumerProperties() {
        Properties result = new Properties();
        Set<String> propertyNames = consumerProperties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            result.setProperty(propertyName, consumerProperties.getProperty(propertyName));
        }
        return result;
    }

    public Properties getBaseProducerProperties() {
        Properties result = new Properties();
        Set<String> propertyNames = producerProperties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            result.setProperty(propertyName, producerProperties.getProperty(propertyName));
        }
        return result;
    }

    protected KafkaConsumer<String, String> createConsumer(Properties properties, ConsumerRebalanceListener consumerRebalanceListener) {
        // 获取需要订阅的topic
        String consumerSubscribeTopicsValue = (String) properties.remove(consumerSubscribeTopicsKey);
        List<String> consumerSubscribeTopics = Arrays.stream(consumerSubscribeTopicsValue.split(",")).map(String::trim).collect(Collectors.toList());

        // 创建consumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        // 订阅topic
        if (!consumerSubscribeTopics.isEmpty()) {
            if (consumerRebalanceListener != null) {
                kafkaConsumer.subscribe(consumerSubscribeTopics, consumerRebalanceListener);
            } else {
                kafkaConsumer.subscribe(consumerSubscribeTopics);
            }
        }

        return kafkaConsumer;
    }

    protected KafkaProducer<String, String> createProducer(Properties properties) {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        return kafkaProducer;
    }
}
