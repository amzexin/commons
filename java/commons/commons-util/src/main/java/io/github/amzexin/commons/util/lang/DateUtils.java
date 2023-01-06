package io.github.amzexin.commons.util.lang;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: DateUtils
 *
 * @author Lizexin
 * @date 2022-09-28 11:38
 */
public class DateUtils {

    public static final String YYYYMMddHHmmssSSS = "YYYY-MM-dd HH:mm:ss.SSS";

    public static String nowDatetime() {
        return new SimpleDateFormat(YYYYMMddHHmmssSSS).format(new Date());
    }

    public static String nowDatetime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }
}
