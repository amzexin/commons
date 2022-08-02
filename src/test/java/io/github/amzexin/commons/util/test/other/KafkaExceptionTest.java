package io.github.amzexin.commons.util.test.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class KafkaExceptionTest {

    @Test
    public void run() {
        poll();
    }

    private void poll() {
        try {
            maybeTriggerWakeup();
        } finally {
            log.info("finally");
        }
    }

    public void maybeTriggerWakeup() {
        throw new RuntimeException();
    }


}
