package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

/**
 * Description: ConcurrentKafkaConsumerCloseException
 *
 * @author Lizexin
 * @date 2022-09-30 11:45
 */
public class CkcNotStartedException extends RuntimeException {

    public CkcNotStartedException() {
    }

    public CkcNotStartedException(String message) {
        super(message);
    }
}
