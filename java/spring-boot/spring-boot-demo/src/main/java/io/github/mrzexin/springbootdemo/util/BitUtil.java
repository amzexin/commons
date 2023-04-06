package io.github.mrzexin.springbootdemo.util;

import org.apache.commons.codec.binary.Hex;

/**
 * BitUtil
 *
 * @author Zexin Li
 * @date 2023-03-29 15:43
 */
public class BitUtil {

    /**
     * 二进制字符串 转 Byte
     *
     * @param binaryStr
     * @return
     */
    public static byte binaryStringToByte(String binaryStr) {
        byte result = 0;
        if (binaryStr == null || binaryStr.length() == 0) {
            return result;
        }
        binaryStr = binaryStr.replaceAll(" ", "");
        int length = binaryStr.length();
        char[] chars = binaryStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ('1' != chars[i]) {
                continue;
            }
            // 由于运算符优先级的原因，等价于 result |= (1 << (length - 1 - i))
            result |= 1 << length - 1 - i;
        }
        return result;
    }

    /**
     * byte 转 二进制字符串
     *
     * @param b
     * @return
     */
    public static String toBinaryString(byte b) {
        return Integer.toBinaryString(b & 0xFF);
    }

    /**
     * byte 转 二进制字符串
     *
     * @param i
     * @return
     */
    public static String toBinaryString(int i) {
        return Integer.toBinaryString(i);
    }

    /**
     * byte to 十六进制
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b) {
        return Hex.encodeHexString(new byte[]{b}, false);
    }
}
