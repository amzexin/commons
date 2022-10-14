package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

import io.github.amzexin.commons.test.kafka.BaseKafkaTest;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Description: ConcurrentConsumeTest
 *
 * @author Lizexin
 * @date 2022-09-28 14:54
 */
@Slf4j
public class ConcurrentKafkaConsumerTest extends BaseKafkaTest {

    private ConcurrentKafkaConsumer concurrentKafkaConsumer;

    public void startConsume(Consumer<ConsumerRecord<String, String>> recordConsumer) {
        Properties consumerProperties = getBaseConsumerProperties();

        // 获取需要订阅的topic
        String consumerSubscribeTopicsValue = (String) consumerProperties.remove(consumerSubscribeTopicsKey);
        List<String> consumerSubscribeTopics = Arrays.stream(consumerSubscribeTopicsValue.split(",")).map(String::trim).collect(Collectors.toList());

        concurrentKafkaConsumer = new ConcurrentKafkaConsumer(consumerProperties);
        log.info("consumer is ok");

        concurrentKafkaConsumer.subscribe(consumerSubscribeTopics, recordConsumer);
        concurrentKafkaConsumer.pollAsync();
    }

    /**
     * 并发消费
     */
    @Test
    public void testConcurrentConsume() throws InterruptedException {
        startConsume(new Consumer() {
            @Override
            public void accept(Object o) {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                String topic = record.topic();
                int partition = record.partition();
                long offset = record.offset();
                // int millis = (new Random().nextInt(3) + 2) * 1000;
                int millis = new Random().nextInt(2000);
                log.info("[{}, {}, {}] >>>>start sleep {}ms, value = {}, ", topic, partition, offset, millis, record.value());
                // 模拟消息处理
                SleepUtils.sleep(millis);
                log.info("[{}, {}, {}] value = {}, end", topic, partition, offset, record.value());
            }
        });

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("是否停止(y/n): ");
            String str = scanner.nextLine();
            if ("y".equalsIgnoreCase(str)) {
                break;
            }
        }
        concurrentKafkaConsumer.close();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>over");
    }

    /**
     * 并发消费在rebalance情况下的测试
     */
    @Test
    public void testConcurrentConsumeInReBalance() throws InterruptedException {
        testConcurrentConsume();
    }

    /**
     * 测试性能
     */
    @Test
    public void testPerformance() {
    }


}
