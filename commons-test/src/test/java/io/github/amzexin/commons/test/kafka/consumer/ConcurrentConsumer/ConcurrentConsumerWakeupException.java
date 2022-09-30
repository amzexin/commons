package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

/**
 * Description: ConcurrentConsumerWakeupException
 *
 * @author Lizexin
 * @date 2022-09-30 11:45
 */
public class ConcurrentConsumerWakeupException extends RuntimeException {

    public ConcurrentConsumerWakeupException() {
    }

    public ConcurrentConsumerWakeupException(String message) {
        super(message);
    }
}
