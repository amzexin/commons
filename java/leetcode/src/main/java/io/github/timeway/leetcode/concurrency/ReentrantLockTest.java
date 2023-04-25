package io.github.timeway.leetcode.concurrency;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLockTest
 *
 * @author Zexin Li
 * @date 2023-04-23 20:33
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    for (int i = 0; i < 10; i++) {
                        System.out.println(Thread.currentThread().getName() + ": sleep 1s");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    reentrantLock.unlock();
                }
            }
        };

        Thread thread1 = new Thread(runnable, "thread1");
        thread1.setDaemon(false);
        thread1.start();

        Thread thread2 = new Thread(runnable, "thread2");
        thread2.setDaemon(false);
        thread2.start();

    }
}
