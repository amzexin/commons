package io.github.amzexin.commons.test.kafka.consumer.ConcurrentConsumer;

/**
 * Description: CkcWakeupException
 *
 * @author Lizexin
 * @date 2022-09-30 11:45
 */
public class CkcWakeupException extends RuntimeException {

    public CkcWakeupException() {
    }

    public CkcWakeupException(String message) {
        super(message);
    }
}
