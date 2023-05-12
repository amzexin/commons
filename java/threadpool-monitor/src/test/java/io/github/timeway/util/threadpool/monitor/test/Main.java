package io.github.timeway.util.threadpool.monitor.test;

import io.github.timeway.util.threadpool.monitor.RejectedTaskCountHandler;
import io.github.timeway.util.threadpool.monitor.ThreadPoolMonitor;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {


        int cpuCount = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                cpuCount,
                cpuCount * 2,
                1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new RejectedTaskCountHandler(new ThreadPoolExecutor.AbortPolicy())
        );

        ThreadPoolMonitor threadPoolMonitor = new ThreadPoolMonitor();
        threadPoolMonitor.addMonitoredThreadPool("test", threadPoolExecutor);
        System.in.read();
        threadPoolMonitor.shutdown();
    }
}