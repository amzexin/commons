package io.github.amzexin.commons.util.test.other;

import io.github.amzexin.commons.util.SleepUtils;
import io.github.amzexin.commons.util.TraceIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: ObjectTest
 *
 * @author Lizexin
 * @date 2022-06-29 14:33
 */
@Slf4j
public class ObjectLockTest {

    private Object lock = new Object();

    @Test
    public void mainTest() throws IOException {
        // synchronized会锁住对象，object.wait会释放该对象的锁
        waitThread();
        waitThread();
        SleepUtils.sleep(1000);
        threadStart(new Runnable() {
            @Override
            public void run() {
                SleepUtils.sleep(4000);
                synchronized (lock) {
                    log.info("进入synchronized");
                    lock.notifyAll();
                    log.info("lock.notifyAll()");
                }
                log.info("退出synchronized");
            }
        }, "notifyThread");

        System.in.read();

    }

    private void threadStart(Runnable runnable, String threadName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TraceIdUtils.setupTraceId();
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    TraceIdUtils.clearTraceId();
                }
            }
        }, threadName).start();
    }

    private AtomicInteger waitThreadNum = new AtomicInteger(0);

    private void waitThread() {
        threadStart(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    log.info("进入synchronized");
                    // SleepUtil.sleep(2000);
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("wait 结束");
                }
                log.info("退出synchronized");
            }
        }, "waitThread-" + waitThreadNum.getAndIncrement());
    }
}
