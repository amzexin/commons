package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Objects;

/**
 * ConsumerRecordWrapper
 *
 * @author zexin
 */
class ConsumerRecordWrapper<K, V> {

    private ConsumerRecord<K, V> consumerRecord;

    private String id;

    private long executeStartTimestamp;

    public ConsumerRecordWrapper(ConsumerRecord<K, V> consumerRecord) {
        this.consumerRecord = consumerRecord;
        this.id = String.format("%s %s %s", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        this.executeStartTimestamp = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerRecordWrapper<?, ?> that = (ConsumerRecordWrapper<?, ?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public long getExecuteStartTimestamp() {
        return executeStartTimestamp;
    }

    public V recordValue() {
        return consumerRecord.value();
    }
}
