package io.github.amzexin.commons.test.gracefulshutdown;

import io.github.amzexin.commons.lang.SleepUtils;
import io.github.amzexin.commons.logback.TraceIdUtils;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.TimeUnit;

/**
 * Description: GracefulShutdownTest
 *
 * @author Lizexin
 * @date 2022-08-03 11:00
 */
@Slf4j
public class GracefulShutdownTest {

    public static void main(String[] args) throws Exception {

        TraceIdUtils.setupTraceId();
        SleepUtils.closePrintLog();

        // 注册kill -15信号
        Signal signal = new Signal("TERM");
        Signal.handle(signal, new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                System.out.println("signal handle: " + signal.getName());
                // 监听信号量，通过System.exit(0)正常关闭JVM，触发关闭钩子执行收尾工作
                System.exit(0);
            }
        });

        // 注册优雅关闭的钩子
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                TraceIdUtils.setupTraceId();
                foreach("优雅关闭", 3);
            }
        }));

        // 进程执行中
        foreach("项目启动", (int) TimeUnit.MINUTES.toSeconds(2));

    }

    private static void foreach(String desc, int count) {
        log.info("{} 开始啦", desc);
        for (int i = 0; i < count; i++) {
            SleepUtils.sleep(1000);
            log.info("{} {}", desc, count - i);
        }
    }
}
