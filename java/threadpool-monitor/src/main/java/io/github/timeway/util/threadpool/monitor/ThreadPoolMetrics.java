package io.github.timeway.util.threadpool.monitor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监控指标
 *
 * @author Zexin Li
 * @date 2023-05-12 17:16
 */
@NoArgsConstructor
@Data
public class ThreadPoolMetrics {
    /**
     * 线程池配置-核心线程数
     */
    private int corePoolSize;
    /**
     * 线程池配置-最大线程数
     */
    private int maximumPoolSize;
    /**
     * 线程池配置-队列类型
     */
    private String queueType;
    /**
     * 线程池配置-拒绝策略
     */
    private String rejectType;
    /**
     * 当前线程数
     */
    private int poolSize;
    /**
     * 当前活跃线程数（正在执行任务的线程数）
     */
    private int activeCount;
    /**
     * 截止目前线程数最大值
     */
    private int largestPoolSize;
    /**
     * 队列当前长度
     */
    private int queueSize;
    /**
     * 队列剩余容量
     */
    private int queueRemainingCapacity;
    /**
     * 已完成的任务数
     */
    private long completedTaskCount;
    /**
     * 拒绝策略执行次数
     */
    private int rejectCount;

    public ThreadPoolMetrics(ThreadPoolExecutor threadPoolExecutor) {
        this.corePoolSize = threadPoolExecutor.getCorePoolSize();
        this.maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
        this.poolSize = threadPoolExecutor.getPoolSize();
        this.activeCount = threadPoolExecutor.getActiveCount();
        this.largestPoolSize = threadPoolExecutor.getLargestPoolSize();
        this.completedTaskCount = threadPoolExecutor.getCompletedTaskCount();

        BlockingQueue<Runnable> blockingQueue = threadPoolExecutor.getQueue();
        this.queueType = blockingQueue.getClass().getName();
        this.queueSize = blockingQueue.size();
        this.queueRemainingCapacity = blockingQueue.remainingCapacity();

        RejectedExecutionHandler rejectedExecutionHandler = threadPoolExecutor.getRejectedExecutionHandler();
        if (rejectedExecutionHandler instanceof RejectedTaskCountHandler) {
            this.rejectType = ((RejectedTaskCountHandler) rejectedExecutionHandler).getRealHandlerClassName();
            this.rejectCount = ((RejectedTaskCountHandler) rejectedExecutionHandler).getRejectedCount();
        } else {
            this.rejectType = rejectedExecutionHandler.getClass().getName();
            this.rejectCount = -1;
        }
    }

    public static ThreadPoolMetrics build(ThreadPoolExecutor threadPoolExecutor) {
        return new ThreadPoolMetrics(threadPoolExecutor);
    }

    public String viewString() {
        return "线程池配置-核心线程数: " + this.corePoolSize + "\n" +
                "线程池配置-最大线程数: " + this.maximumPoolSize + "\n" +
                "线程池配置-队列类型: " + this.queueType + "\n" +
                "线程池配置-拒绝策略: " + this.rejectType + "\n" +
                "当前线程数: " + this.poolSize + "\n" +
                "当前活跃线程数（正在执行任务的线程数）: " + this.activeCount + "\n" +
                "截止目前线程数最大值: " + this.largestPoolSize + "\n" +
                "队列当前长度: " + this.queueSize + "\n" +
                "队列剩余容量: " + this.queueRemainingCapacity + "\n" +
                "已完成的任务数: " + this.completedTaskCount + "\n" +
                "拒绝策略执行次数: " + this.rejectCount;
    }
}
