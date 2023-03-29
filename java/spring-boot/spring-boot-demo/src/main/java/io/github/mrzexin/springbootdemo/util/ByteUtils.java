package io.github.mrzexin.springbootdemo.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 主要用于执行字节数组和基本数据类型之间的互相转换 比如字节数组转int，和int转字节数组 java是属于大端字节序的，也就是高位放在低地址处
 * 所有的操作都是针对的大端字节序
 */
public class ByteUtils {

    private static String hexStr = "0123456789ABCDEF";

    private static String[] binaryArray = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000",
            "1001", "1010", "1011", "1100", "1101", "1110", "1111"};

    /**
     * 字节数组转short
     *
     * @param bytes 需要转换的字节数组
     * @return short
     */
    public static short byteArrToShort(byte[] bytes) {
        int FF = 0xff;
        int first = (bytes[0] & FF) << 8;
        short result = (short) (first | (bytes[1] & FF));
        return result;
        // 可以直接return (short) ((bytes[0] << 8) | bytes[1]);
        // 之所以向上面那么写，是为了方便看的清楚
    }

    /**
     * short转字节数组
     *
     * @param value 待转换的short
     * @return 字节数组
     */
    public static byte[] shortToByteArr(short value) {
        byte highByte = (byte) (value >> 8);// 获取高位的字节
        byte lowByte = (byte) value;// 获取低位的字节
        byte[] result = new byte[2];
        result[0] = highByte;
        result[1] = lowByte;
        return result;
    }

    /**
     * 字节数组转int
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的int
     */
    public static int byteArrToInt(byte[] bytes) {
        if (bytes.length > 4) {
            // java中1左移超过32位时，会先对位移次数取模，结果将不会符合预期，直接抛出
            // 可参考：https://blog.csdn.net/keep12moving/article/details/103109092
            throw new RuntimeException("bytes.length > 4");
        }
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result |= bytes[i] << (bytes.length - i - 1) * 8;
        }
        return result;
    }

    /**
     * 字节数组转int
     *
     * @param bytes  待转换的字节数组
     * @param from   其实位置
     * @param offset 获取位数
     * @return 转换之后的int
     */
    public static int byteArrToInt(byte[] bytes, int from, int offset) {
        byte[] copy = new byte[offset];
        System.arraycopy(bytes, from, copy, 0, offset);
        return byteArrToInt(copy);
    }

    /**
     * int转字节数组
     *
     * @param value 待转换的int
     * @return 转换之后的字节数组
     */
    public static byte[] intToByteArr(int value, int size) {
        // 假设原来的int的16进制为 05 0A 07 04
        if (size >= 0 && size <= Integer.SIZE / 8) {
            // 假设待转换的long为08 0A 01 03 05 07 02 0B
            byte[] bytes = new byte[size];
            for (int i = 0; i < size; i++) {
                int bit = (size - i - 1) * 8;
                bytes[i] = (byte) (value >> bit);
            }
            return bytes;
        }
        return null;
    }

    /**
     * 把字节数组转换成long 这个的处理手法和前面和int和short都有一点差异
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的long
     */
    public static long byteArrToLong(byte[] bytes) {
        long FF = 0xff;
        // 假设字节数组为08 0A 01 03 05 07 02 0B
        /*
         * long b0 = bytes[0] & FF;//00 00 00 00 00 00 00 08 long h0 = b0 << 56;//08 00
         * 00 00 00 00 00 00 long b1 = bytes[1] & FF;//00 00 00 00 00 00 00 0A long h1 =
         * b1 << 48;//00 0A 00 00 00 00 00 00 long b2 = bytes[2] & FF; long h2 = b2 <<
         * 40; long b3 = bytes[3] & FF; long h3 = b3 << 32; long b4 = bytes[4] & FF;
         * long h4 = b4 << 24; long b5 = bytes[5] & FF; long h5 = b5 << 16; long b6 =
         * bytes[6] & FF; long h6 = b6 << 8; long b7 = bytes[7] & FF; long h7 = b7;
         */

        // return h0 | h1 | h2 | h3 | h4 | h5 | h6 | h7;
        long result = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            int bit = (length - i - 1) * 8;
            long b = bytes[i] & FF;
            long h = b << bit;
            // System.out.println(b+"----"+h);
            result = result | h;
        }
        return result;

    }

    /**
     * long转换成字节数组
     *
     * @param value 待转换的long
     * @return 转换之后的字节数组
     */
    public static byte[] longToByteArr(long value, int size) {
        if (size >= 0 && size <= Long.SIZE / 8) {
            // 假设待转换的long为08 0A 01 03 05 07 02 0B
            byte[] bytes = new byte[size];
            for (int i = 0; i < size; i++) {
                int bit = (size - i - 1) * 8;
                bytes[i] = (byte) (value >> bit);
            }
            return bytes;
        }
        return null;
        /*
         * bytes[0] = (byte) (value >> 56);//右移之后,00 00 00 00 00 00 00 08,强转之后08
         * bytes[1] = (byte) (value >> 48);//右移之后,00 00 00 00 00 00 08 0A,强转之后0A
         * bytes[2] = (byte) (value >> 40);//右移之后,00 00 00 00 00 08 0A 01,强转之后01
         * bytes[3] = (byte) (value >> 32); bytes[4] = (byte) (value >> 24); bytes[5] =
         * (byte) (value >> 16); bytes[6] = (byte) (value >> 8); bytes[7] = (byte)
         * value; return bytes;
         */
    }

    /**
     * 将字节数组转换成float
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的float
     */
    public static float byteArrToFloat(byte[] bytes) {
        int FF = 0xff;
        // 假设字节数组 05 0A 07 04
        int b0 = bytes[0] & FF;// 00 00 00 05
        int b1 = bytes[1] & FF;// 00 00 00 0A
        int b2 = bytes[2] & FF;
        int b3 = bytes[3] & FF;
        int h0 = b0 << 24;// 05 00 00 00
        int h1 = b1 << 16;// 00 0A 00 00
        int h2 = b2 << 8;
        int h3 = b3;
        int h = h0 | h1 | h2 | h3;
        return Float.intBitsToFloat(h);
    }

    /**
     * float转字节数组
     *
     * @param value 待转换的float
     * @return 转换之后的字节数组
     */
    public static byte[] floatToByteArr(float value) {
        int i = Float.floatToIntBits(value);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) (i >> 16);
        bytes[2] = (byte) (i >> 8);
        bytes[3] = (byte) (i);
        return bytes;
    }

    /**
     * 将字节数组转换成double
     *
     * @param bytes 待转换的字节数组
     * @return 转换出来的double
     */
    public static double byteArrToDouble(byte[] bytes) {
        // 假设待转换的字节数组为05 0A 07 04 0B 00 03 01
        long FF = 0xFF;
        long b0 = bytes[0] & FF;// 00 00 00 00 00 00 00 05
        long b1 = bytes[1] & FF;// 00 00 00 00 00 00 00 0A
        long b2 = bytes[2] & FF;
        long b3 = bytes[3] & FF;
        long b4 = bytes[4] & FF;
        long b5 = bytes[5] & FF;
        long b6 = bytes[6] & FF;
        long b7 = bytes[7] & FF;
        long h0 = b0 << 56;// 05 00 00 00 00 00 00 00
        long h1 = b1 << 48;// 00 0A 00 00 00 00 00 00
        long h2 = b2 << 40;
        long h3 = b3 << 32;
        long h4 = b4 << 24;
        long h5 = b5 << 16;
        long h6 = b6 << 8;
        long h7 = b7;
        long h = h0 | h1 | h2 | h3 | h4 | h5 | h6 | h7;
        return Double.longBitsToDouble(h);
    }

    /**
     * 将double转换成字节数组
     *
     * @param value 待转换的double
     * @return 转换之后的字节数组
     */
    public static byte[] doubleToByteArr(double value) {
        long lbits = Double.doubleToLongBits(value);
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (lbits >> 56);
        bytes[1] = (byte) (lbits >> 48);
        bytes[2] = (byte) (lbits >> 40);
        bytes[3] = (byte) (lbits >> 32);
        bytes[4] = (byte) (lbits >> 24);
        bytes[5] = (byte) (lbits >> 16);
        bytes[6] = (byte) (lbits >> 8);
        bytes[7] = (byte) (lbits);
        return bytes;
    }

    /**
     * 将字节数组转换成十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的十六进制字符串
     */
    public static String bytesToHexStr(byte[] bytes) {
        return Hex.encodeHexString(bytes, false);
    }

    /**
     * 字节数组反转
     *
     * @param bytes
     * @return
     */
    public static byte[] reverse(byte[] bytes) {
        byte[] b = new byte[bytes.length];
        int j = bytes.length;
        for (int i = 0; i < bytes.length; i++) {
            b[j - 1] = bytes[i];
            j = j - 1;
        }
        return b;
    }


    /**
     * 将16进制转换成二进制字节数组,注意16进制不要输入0x，只需输入ff，不要输入成0xff
     *
     * @param hexString 带转换的十六进制字符串
     * @return 转换之后的字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 将16进制转换成二进制字节数组,注意16进制不要输入0x，只需输入ff，不要输入成0xff
     *
     * @param hexString 带转换的十六进制字符串
     * @return 转换之后的字节数组
     */
    public static byte[] hexStringToBytes(String hexString, int len) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        if (length < len) {
            for (int i = 0; i < len - length; i++) {
                hexString = "00" + hexString;
            }
            length = len;
        }
        byte[] d = new byte[length];
        char[] hexChars = hexString.toCharArray();
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 将byte数组转换为指定编码格式的普通字符串
     *
     * @param bytes   byte数组
     * @param charset 编码格式 可不传默认为Charset.defaultCharset()
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String byteToString(byte[] bytes, String charset) {
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 二进制数组转换为二进制字符串 2-2
     */
    public static String bytesToBinStr(byte[] bArray) {
        String outStr = "";
        int pos = 0;
        for (byte b : bArray) {
            // 高四位
            pos = (b & 0xF0) >> 4;
            outStr += binaryArray[pos];
            // 低四位
            pos = b & 0x0F;
            outStr += binaryArray[pos];
        }
        return outStr;
    }

    /**
     * 字符转指定长度的byte数组
     *
     * @param str
     * @param size
     * @return
     */
    public static byte[] stringToByte(String str, int size) {
        try {
            if (StringUtils.isEmpty(str)) {
                str = "";
            }

            byte[] bytes = str.getBytes("utf-8");
            if (size == bytes.length) {
                return bytes;
            }

            if (size > bytes.length) {
                byte[] result = new byte[size];
                for (int i = 0; i < size; i++) {
                    if (i < bytes.length) {
                        result[i] = bytes[i];
                    } else {
                        result[i] = 0;
                    }
                }
                return result;
            } else {
                return bytes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按顺序合并多个byte[]
     *
     * @param byteArray
     * @return
     */
    public static byte[] byteMerger(List<byte[]> byteArray) {
        int len = 0;
        for (byte[] b : byteArray) {
            len += b.length;
        }
        byte[] result = new byte[len];

        int point = 0;
        for (byte[] b : byteArray) {
            System.arraycopy(b, 0, result, point, b.length);
            point += b.length;
        }

        return result;
    }

    /**
     * 将十六进制字符串转换为二进制字符串
     */
    public static String hexStr2BinStr(String hexString) {
        return bytesToBinStr(hexStringToBytes(hexString));
    }

    /**
     * 获取位对象返回十进制数值
     *
     * @param data
     * @param start
     * @param end
     * @return
     */
    public static int getBit(byte[] data, int start, int end) {
        int startIndex = start / 8;
        int endIndex = end / 8;

        StringBuilder bitString = new StringBuilder();
        for (int index = startIndex; index <= endIndex; index++) {
            bitString.append(byteToBit(data[index]));
        }
        String target = bitString.substring(start % 8, (endIndex - startIndex) * 8 + end % 8 + 1);
        int bit = 0;
        for (char chars : target.toCharArray()) {
            bit = Character.getNumericValue(chars) + bit * 2;
        }
        return bit;
    }

    /**
     * byte转bit
     *
     * @param by
     * @return
     */
    public static String byteToBit(byte by) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((by >> 7) & 0x1).append((by >> 6) & 0x1).append((by >> 5) & 0x1).append((by >> 4) & 0x1)
                .append((by >> 3) & 0x1).append((by >> 2) & 0x1).append((by >> 1) & 0x1).append((by) & 0x1);
        return stringBuilder.toString();
    }

    /**
     * 复制byte[]数组
     *
     * @param source     原字节数组
     * @param startIndex 复制起始位置
     * @return
     */
    public static byte[] copyArray(byte[] source, int startIndex) {
        int length = source.length - startIndex;
        byte[] target = new byte[length];
        System.arraycopy(source, startIndex, target, 0, length);
        return target;
    }

    /**
     * 将byte数组无符号int
     *
     * @param bytes
     * @return
     */
    public static BigInteger byteArrToUnsignedInt(byte[] bytes) {
        byte[] b2 = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, b2, 1, bytes.length);
        return new BigInteger(b2);
    }

    public static String strArrToHex(int[] source) {
        StringBuilder middle = new StringBuilder();
        StringBuilder target = new StringBuilder();
        for (int i = 0; i < source.length; i++) {
            middle.append(source[i]);
            if ((i + 1) % 8 == 0) {
                int midInt = Integer.parseInt(middle.toString(), 2);
                target.append(Integer.toHexString(midInt).toUpperCase());
                middle = new StringBuilder();
            }
        }
        if (middle.length() > 0) {
            int length = middle.length();
            for (int i = 0; i < 8 - length; i++) {
                middle.append("0");
            }
            target.append(Integer.toHexString(Integer.parseInt(middle.toString(), 2)).toUpperCase());
        }
        return target.toString();
    }

    /**
     * 获取指令签名
     *
     * @param deviceSecret
     * @param timestamp
     * @return
     */
    public static String getCmdSign(String deviceSecret, Long timestamp) {
        byte[] b1 = ByteUtils.stringToByte(deviceSecret, 16);
        byte[] b2 = ByteUtils.longToByteArr(timestamp, 6);
        List<byte[]> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        byte[] b3 = byteMerger(list);
        return ByteUtils.bytesToHexStr(DigestUtils.md5(b3)).substring(12, 32);
    }

    /**
     * 获取指令签名  来自统计模块整合
     *
     * @param deviceSecret
     * @param timestamp
     * @param sn
     * @return
     */
    public static String getCmdSign(String deviceSecret, Long timestamp, String sn) {
        byte[] b1 = ByteUtils.stringToByte(deviceSecret, 16);
        byte[] b2 = ByteUtils.longToByteArr(timestamp, 6);
        List<byte[]> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);

        if (StringUtils.isNoneBlank(sn)) {
            byte[] b3 = ByteUtils.stringToByte(sn, 14);
            list.add(b3);
        }
        byte[] b4 = byteMerger(list);
        return ByteUtils.bytesToHexStr(DigestUtils.md5(b4)).substring(12, 32);
    }


    /**
     * 获取指令签名 by SN
     *
     * @param deviceSecret
     * @param timestamp
     * @return
     */
    public static String getCmdSignBySn(String deviceSecret, Long timestamp, String sn) {
        byte[] b1 = ByteUtils.stringToByte(deviceSecret, 16);
        byte[] b2 = ByteUtils.longToByteArr(timestamp, 6);
        byte[] b3 = ByteUtils.stringToByte(sn, 14);

        List<byte[]> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        list.add(b3);

        byte[] b4 = byteMerger(list);
        return ByteUtils.bytesToHexStr(DigestUtils.md5(b4)).substring(12, 32);
    }


    public static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }


    public static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }

    /**
     * 获取小端数据
     *
     * @param buf
     * @return
     */
    public static String parseByte2LeHexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = buf.length - 1; i >= 0; i--) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 获取大端数据
     *
     * @param buf
     * @return
     */
    public static String parseByte2BeHexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = buf.length - 1; i >= 0; i--) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 获取通用指令校验和
     *
     * @param data
     * @return
     */
    public static String getCheckSum(String data) {
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        String hex1 = hexInt(total);
        byte[] bytes = parseHexStr2Byte(hex1);
        byte temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = bytes[i];
            bytes[i] = (byte) (~temp);
        }
        return parseByte2LeHexStr(bytes);
    }

    /**
     * 判断是否包含不可见ascii码
     *
     * @param bytes
     * @return
     */
    public static boolean isContainAsciiControl(byte[] bytes) {
        if (null == bytes) {
            return false;
        }
        for (byte b : bytes) {
            if (b <= 32 || b == 127) {
                return true;
            }
        }
        return false;
    }

    public static String byteToBinaryStr(byte b) {
        StringBuilder result = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            result.append(b & (1 << i));
        }
        return result.reverse().toString();
    }
}