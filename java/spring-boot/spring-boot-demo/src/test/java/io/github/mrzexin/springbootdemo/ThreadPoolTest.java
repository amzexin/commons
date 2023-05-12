package io.github.mrzexin.springbootdemo;

import ch.qos.logback.core.util.TimeUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolTest
 *
 * @author Zexin Li
 * @date 2023-05-12 15:52
 */
public class ThreadPoolTest {

    @Test
    public void test20230512_1552() {
        int cpuCount = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(cpuCount, cpuCount * 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        // 线程池配置-核心线程数
        int corePoolSize = threadPoolExecutor.getCorePoolSize();

        // 线程池配置-最大线程数
        int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();

        // 线程池配置-当前队列
        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();
        String queueType = queue.getClass().getName();

        // 当前线程数
        int poolSize = threadPoolExecutor.getPoolSize();

        // 当前活跃线程数（正在执行任务的线程数）
        int activeCount = threadPoolExecutor.getActiveCount();

        // 截止目前线程数最大值
        int largestPoolSize = threadPoolExecutor.getLargestPoolSize();

        // 当前队列长度
        int queueSize = queue.size();

        // 队列剩余容量
        int remainingCapacity = queue.remainingCapacity();

        // 已完成的任务数
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();

        // 任务总数 = 已完成的任务数 + 正在执行的任务数 + 队列里的任务数
        long taskCount = threadPoolExecutor.getTaskCount();

        // 拒绝策略执行次数


    }
}
