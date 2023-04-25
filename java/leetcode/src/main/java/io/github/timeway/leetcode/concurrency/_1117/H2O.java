package io.github.timeway.leetcode.concurrency._1117;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://leetcode.cn/problems/building-h2o/
 */
public class H2O {

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition cond = lock.newCondition();

    private int hCount = 0;

    private int oCount = 0;

    public H2O() {

    }

    private void openBarrier() {
        if (hCount == 2 && oCount == 1) {
            hCount = 0;
            oCount = 0;
            cond.signalAll();
        }
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        lock.lock();
        try {
            while (hCount == 2) {
                cond.await();
            }
            hCount++;
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            openBarrier();
        } finally {
            lock.unlock();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        lock.lock();
        try {
            while (oCount == 1) {
                cond.await();
            }
            oCount++;
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            openBarrier();
        } finally {
            lock.unlock();
        }

    }
}
