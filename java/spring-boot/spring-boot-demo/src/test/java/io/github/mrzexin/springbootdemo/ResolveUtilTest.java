package io.github.mrzexin.springbootdemo;


import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * ResolveUtilTestt
 *
 * @author Zexin Li
 * @date 2023-03-27 17:07
 */
public class ResolveUtilTest {
    /**
     * 解析带0x00占为的ASCII码
     *
     * @param dfBytes
     * @return
     */
    public static String resolveExASCII(byte[] dfBytes, int cursor, int length) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, cursor, cursor + length);
        String asciiStr = resolveASCIIStr(bytes);
        return StringUtils.replace(asciiStr, "\u0000", "");
    }

    /**
     * 解析字符串-ascii码
     *
     * @param bytes
     * @return
     */
    public static String resolveASCIIStr(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        String str = "abcdefg";
        byte[] bytes = str.getBytes();
        int maxLength = 16;
        if (bytes.length < maxLength) {
            byte[] newBytes = new byte[maxLength];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            for (int i = bytes.length; i < maxLength; i++) {
                newBytes[i] = 0;
            }
            bytes = newBytes;
        }

        for (int i = 0; i < bytes.length; i++) {
            System.out.println(i + ": " + bytes[i]);
        }

        System.out.println(resolveExASCII(bytes, 0, bytes.length));
    }
}
