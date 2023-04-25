package io.github.timeway.leetcode.concurrency;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrierTest
 *
 * @author Zexin Li
 * @date 2023-04-23 20:34
 */
public class CyclicBarrierTest {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException, IOException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, null);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + ": 等待");
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + ": 过栅栏了");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }
        };


        for (int i = 0; i < 9; i++) {
            Thread thread = new Thread(runnable, "thread" + i);
            thread.setDaemon(false);
            thread.start();
        }

        System.in.read();
        Thread thread10 = new Thread(runnable, "thread10");
        thread10.setDaemon(false);
        thread10.start();
    }
}
