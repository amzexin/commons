package io.github.timeway.util.threadpool.monitor;

public interface ThreadPoolMetricsOperator {
    default void operate(String threadPoolName, ThreadPoolMetrics threadPoolMetrics) {
        System.out.println(threadPoolName + " --- " + threadPoolMetrics);
    }
}
