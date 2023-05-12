package io.github.timeway.util.threadpool.monitor;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池监控者
 *
 * @author Zexin Li
 * @date 2023-05-12 17:34
 */
public class ThreadPoolMonitor {

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService threadPoolMonitorExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "ThreadPoolMonitor"));

    public ThreadPoolMonitor() {
        this(new ThreadPoolMetricsOperator() {
            @Override
            public void operate(String threadPoolName, ThreadPoolMetrics threadPoolMetrics) {
                ThreadPoolMetricsOperator.super.operate(threadPoolName, threadPoolMetrics);
            }
        });
    }

    public ThreadPoolMonitor(ThreadPoolMetricsOperator threadPoolMetricsOperator) {
        threadPoolMonitorExecutor.scheduleWithFixedDelay(() -> {
            if (threadPoolExecutorMap.isEmpty()) {
                return;
            }

            threadPoolExecutorMap.forEach((threadPoolName, threadPoolExecutor) ->
                    threadPoolMetricsOperator.operate(threadPoolName, ThreadPoolMetrics.build(threadPoolExecutor))
            );
        }, 3, 1, TimeUnit.SECONDS);
    }

    /**
     * 添加需要被监控的线程池
     *
     * @param threadPoolName
     * @param threadPoolExecutor
     */
    public void addMonitoredThreadPool(String threadPoolName, ThreadPoolExecutor threadPoolExecutor) {
        if (threadPoolMonitorExecutor.isShutdown()) {
            throw new RejectedExecutionException("threadPoolExecutor is shutdown");
        }
        threadPoolExecutorMap.put(threadPoolName, threadPoolExecutor);
    }

    /**
     * 移除被监控的线程池
     *
     * @param threadPoolName
     */
    public void removeMonitoredThreadPool(String threadPoolName) {
        threadPoolExecutorMap.remove(threadPoolName);
    }

    /**
     * 终止监控
     */
    public void shutdown() {
        if (threadPoolMonitorExecutor.isShutdown()) {
            return;
        }
        threadPoolExecutorMap.clear();
        threadPoolMonitorExecutor.shutdown();
    }

}
