package io.github.timeway.util.threadpool.monitor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RejectedTaskCountHandler
 *
 * @author Zexin Li
 * @date 2023-05-12 19:59
 */
public class RejectedTaskCountHandler implements RejectedExecutionHandler {

    private final RejectedExecutionHandler realHandler;

    private final AtomicInteger rejectedCount = new AtomicInteger(0);

    public RejectedTaskCountHandler(RejectedExecutionHandler realHandler) {
        this.realHandler = realHandler;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        rejectedCount.incrementAndGet();
        realHandler.rejectedExecution(r, executor);
    }

    public String getRealHandlerClassName() {
        return realHandler.getClass().getName();
    }

    public int getRejectedCount() {
        return rejectedCount.get();
    }
}
