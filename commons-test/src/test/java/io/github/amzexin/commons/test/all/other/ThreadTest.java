package io.github.amzexin.commons.test.all.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Description: ThreadTest
 *
 * @author Lizexin
 * @date 2022-09-02 16:13
 */
@Slf4j
public class ThreadTest {

    @Test
    public void testInterrupt() throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        });

        thread.start();
        log.info("starting。。。");
        Thread.sleep(1000);

        thread.interrupt();
        System.in.read();
    }
}
