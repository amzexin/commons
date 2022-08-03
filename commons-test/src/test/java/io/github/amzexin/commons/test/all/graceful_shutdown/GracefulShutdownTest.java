package io.github.amzexin.commons.test.all.graceful_shutdown;

import io.github.amzexin.commons.lang.SleepUtils;
import io.github.amzexin.commons.logback.TraceIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Description: GracefulShutdownTest
 *
 * @author Lizexin
 * @date 2022-08-03 11:00
 */
@Slf4j
public class GracefulShutdownTest {

    @Test
    public void test20220803_1100() {
        TraceIdUtils.setupTraceId();
        SleepUtils.closePrintLog();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                TraceIdUtils.setupTraceId();
                log.info("优雅关闭啦");
                int count = 3;
                for (int i = 0; i < count; i++) {
                    SleepUtils.sleep(1000);
                    log.info("优雅关闭 {}", count - i);
                }
            }
        }));

        log.info("项目启动");
        int count = 10;
        for (int i = 0; i < count; i++) {
            SleepUtils.sleep(1000);
            log.info("项目启动 {}", count - i);
        }
    }
}
