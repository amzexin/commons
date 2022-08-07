package io.github.amzexin.commons.util.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description: SleepUtils
 *
 * @author Lizexin
 * @date 2022-06-09 17:42
 */
public class SleepUtils {

    private static final Logger log = LoggerFactory.getLogger(SleepUtils.class);

    private static final AtomicBoolean printLog = new AtomicBoolean(false);

    public static void openPrintLog() {
        if (printLog.compareAndSet(false, true)) {
            log.info("SleepUtils openPrintLog...");
        }
    }

    public static void closePrintLog() {
        if (printLog.compareAndSet(true, false)) {
            log.info("SleepUtils closePrintLog...");
        }
    }

    public static void sleep(long millis) {
        try {
            if (printLog.get()) {
                log.info("sleep {}ms start...", millis);
            }
            Thread.sleep(millis);
            if (printLog.get()) {
                log.info("sleep {}ms end...", millis);
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private SleepUtils() {
    }
}
