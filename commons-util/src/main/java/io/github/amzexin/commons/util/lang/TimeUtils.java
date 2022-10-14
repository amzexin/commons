package io.github.amzexin.commons.util.lang;

/**
 * Description: MemoryUtil
 *
 * @author Lizexin
 * @date 2022-09-07 16:23
 */
public class TimeUtils {

    private static final long SECOND = 1000;

    private static final long MINUTE = SECOND * 60;

    private static final long HOUR = MINUTE * 60;

    private static final long DAY = HOUR * 24;

    /**
     * 时间可读化
     *
     * @return
     */
    public static String timeHumanize(long millis) {
        if (millis <= 0) {
            return "";
        }
        if (millis >= DAY) {
            return millis / DAY + "d" + timeHumanize(millis % DAY);
        }
        if (millis >= HOUR) {
            return millis / HOUR + "h" + timeHumanize(millis % HOUR);
        }
        if (millis >= MINUTE) {
            return millis / MINUTE + "m " + timeHumanize(millis % MINUTE);
        }
        if (millis >= SECOND) {
            return millis / SECOND + "s " + timeHumanize(millis % SECOND);
        }
        return millis + "ms";
    }

    public static void main(String[] args) {
        System.out.println(timeHumanize(60000));
    }
}
