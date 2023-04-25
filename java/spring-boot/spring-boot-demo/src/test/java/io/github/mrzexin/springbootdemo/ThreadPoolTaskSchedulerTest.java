package io.github.mrzexin.springbootdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Duration;

/**
 * ThreadPoolTaskSchedulerTest
 *
 * @author Zexin Li
 * @date 2023-04-25 15:27
 */
@Slf4j
public class ThreadPoolTaskSchedulerTest {

    @Test
    public void testThreadPoolTaskScheduler() throws InterruptedException {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("iot-hub-task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);

        scheduler.initialize();

        scheduler.scheduleAtFixedRate(() -> log.info("lalala 2s"), Duration.ofSeconds(2));
        scheduler.scheduleAtFixedRate(() -> log.info("lalala 2m"), Duration.ofMinutes(2));
        Thread.sleep(5000);

        scheduler.shutdown();
    }

    @Test
    public void testScheduledTaskRegistrar() throws InterruptedException {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4);
        scheduler.setThreadNamePrefix("my-scheduler");
        scheduler.setAwaitTerminationSeconds(20);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.initialize();

        ScheduledTaskRegistrar scheduledTaskRegistrar = new ScheduledTaskRegistrar();
        // 2s 执行一次
        scheduledTaskRegistrar.addCronTask(new CronTask(() -> log.info("lalala 2s"), "0/2 * * * * ?"));
        // 2min 执行一次
        /**
         * 项目中使用了 @Scheduled，通过 cron 方式定时触发，并开启了 waitForTasksToCompleteOnShutdown。
         * 项目在关闭的时候，有可能会因为一直没有到执行时间，最后超时关闭线程池。可通过监控线程池任务数得知。
         * fixDelay 应该是执行当前这一次之后，把下一次的添加进去的
         * cron 应该是在执行当前这一次之前，把下一次的添加进去的
         */
        scheduledTaskRegistrar.addCronTask(new CronTask(() -> log.info("lalala 2s"), "0 */2 * * * ?"));
        // 2min 固定延迟
        scheduledTaskRegistrar.addFixedDelayTask(new IntervalTask(() -> log.info("lalala 2min FixedDelayTask"), 2 * 60 * 1000));
        scheduledTaskRegistrar.setTaskScheduler(scheduler);
        scheduledTaskRegistrar.afterPropertiesSet();

        Thread.sleep(5000);

        scheduler.shutdown();

    }
}
