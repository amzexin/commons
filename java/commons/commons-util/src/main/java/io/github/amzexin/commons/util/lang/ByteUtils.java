package io.github.amzexin.commons.util.lang;

/**
 * Description: MemoryUtil
 *
 * @author Lizexin
 * @date 2022-09-07 16:23
 */
public class ByteUtils {

    private static final long KB = 1024;

    private static final long MB = KB * 1024;

    private static final long GB = MB * 1024;

    private static final long TB = GB * 1024;

    private static final long PB = TB * 1024;

    /**
     * 字节可读化
     *
     * @return
     */
    public static String byteHumanize(long size) {
        if (size <= 0) {
            return "";
        }
        if (size >= PB) {
            return size / PB + "PB" + byteHumanize(size % PB);
        }
        if (size >= TB) {
            return size / TB + "TB" + byteHumanize(size % TB);
        }
        if (size >= GB) {
            return size / GB + "GB" + byteHumanize(size % GB);
        }
        if (size >= MB) {
            return size / MB + "MB " + byteHumanize(size % MB);
        }
        if (size >= KB) {
            return size / KB + "KB " + byteHumanize(size % KB);
        }
        return size + "B";
    }

    public static void main(String[] args) {
        System.out.println(byteHumanize(MB * MB));
    }
}
