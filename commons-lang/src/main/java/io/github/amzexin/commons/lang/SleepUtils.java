package io.github.amzexin.commons.lang;

import lombok.extern.slf4j.Slf4j;

/**
 * Description: SleepUtils
 *
 * @author Lizexin
 * @date 2022-06-09 17:42
 */
@Slf4j
public class SleepUtils {

    public static void sleep(long millis) {
        try {
            log.info("sleep {}ms start...", millis);
            Thread.sleep(millis);
            log.info("sleep {}ms end...", millis);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private SleepUtils() {
    }
}
