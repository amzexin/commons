package io.github.amzexin.commons.util.lang;

import java.time.Duration;

/**
 * Description: MemoryUtil
 *
 * @author Lizexin
 * @date 2022-09-07 16:23
 */
public class TimeUtils {

    private static final long NANOSECOND = 1;

    private static final long MICROSECOND = NANOSECOND * 1000;

    private static final long MILLISECOND = MICROSECOND * 1000;

    private static final long SECOND = MILLISECOND * 1000;

    private static final long MINUTE = SECOND * 60;

    private static final long HOUR = MINUTE * 60;

    private static final long DAY = HOUR * 24;

    /**
     * 毫秒人性化
     *
     * @return
     */
    public static String millisHumanize(long millis) {
        return nanosHumanize(Duration.ofMillis(millis).toNanos());
    }

    /**
     * 纳秒人性化
     *
     * @return
     */
    public static String nanosHumanize(long nanos) {
        if (nanos <= 0) {
            return "";
        }
        if (nanos >= DAY) {
            return nanos / DAY + "d " + nanosHumanize(nanos % DAY);
        }
        if (nanos >= HOUR) {
            return nanos / HOUR + "h " + nanosHumanize(nanos % HOUR);
        }
        if (nanos >= MINUTE) {
            return nanos / MINUTE + "m " + nanosHumanize(nanos % MINUTE);
        }
        if (nanos >= SECOND) {
            return nanos / SECOND + "s " + nanosHumanize(nanos % SECOND);
        }
        if (nanos >= MILLISECOND) {
            return nanos / MILLISECOND + "ms " + nanosHumanize(nanos % MILLISECOND);
        }
        if (nanos >= MICROSECOND) {
            return nanos / MICROSECOND + "μs " + nanosHumanize(nanos % MICROSECOND);
        }
        return nanos + "ns";
    }

    public static void main(String[] args) {

        System.out.println(millisHumanize(Duration.ofMillis(100000000L).toMillis()));
    }
}
