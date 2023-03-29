package io.github.mrzexin.springbootdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/**
 * 解析util
 */
@Slf4j
public class ResolveUtil {

    /**
     * 经度偏移量
     */
    private static final int LONGITUDE_OFFSET = 180;
    /**
     * 纬度偏移量
     */
    private static final int LATITUDE_OFFSET = 90;
    /**
     * 经纬度精度
     */
    private static final double GPS_PRECISION = 0.0000001;

    private static int currentYear = 0;

    static {
        DateFormat df = new SimpleDateFormat("yy");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        currentYear = Integer.parseInt(formattedDate);
    }

    /**
     * 解析时间戳
     *
     * @param playload
     * @param start    左闭
     * @param end      右开
     * @return
     */
    public static long resolveTimestamp(byte[] playload, int start, int end) {
        byte[] bytes = Arrays.copyOfRange(playload, start, end);
        return ByteUtils.byteArrToLong(bytes);
    }

    /**
     * 解析流水号
     *
     * @param playload
     * @return
     */
    public static int resolveSerialNumber(byte[] playload, int start, int end) {
        byte[] bytes = Arrays.copyOfRange(playload, start, end);
        return ByteUtils.byteArrToInt(bytes);
    }

    /**
     * 解析协议体版本号
     *
     * @param playload
     * @return
     */
    public static int resolveVersionCode(byte[] playload, int start, int end) {
        byte[] bytes = Arrays.copyOfRange(playload, start, end);
        return ByteUtils.byteArrToInt(bytes);
    }

    /**
     * 解析数据流类型标示
     *
     * @param body
     * @param dfCodeLength
     * @return
     */
    public static String resolveDataFlowCode(byte[] body, int dfCodeLength) {
        byte[] bytes = Arrays.copyOfRange(body, 0, dfCodeLength);
        return ByteUtils.bytesToHexStr(bytes);
    }


    /**
     * 解析数据体长度
     *
     * @param body
     * @param dfCodeLength
     * @param dfBodyLength
     * @return
     */
    public static int resolveDftLength(byte[] body, int dfCodeLength, int dfBodyLength) {
        byte[] bytes = Arrays.copyOfRange(body, dfCodeLength, dfCodeLength + dfBodyLength);
        //log.debug("dfLengthHex:{} deviceName:{}",ByteUtils.bytesToHexStr(bytes),ThreadContext.getThreadAttr(Constants.THREAD_DEVICE_NAME_VAR));
        return ByteUtils.byteArrToInt(bytes);
    }

    /**
     * 解析有符号整数(带偏移量)
     *
     * @param bytes
     * @return
     */
    public static Integer resolveInt(byte[] bytes, Integer attrOffset) {
        Integer tempint = ByteUtils.byteArrToInt(bytes);
        if (null != attrOffset) {
            return tempint - attrOffset;
        }
        return tempint;
    }

    /**
     * 解析有符号整数
     *
     * @param bytes
     * @return
     */
    public static Integer resolveInt(byte[] bytes) {
        return resolveInt(bytes, null);
    }

    /**
     * 解析浮点数
     *
     * @param bytes
     * @return
     */
    public static Double resolveFloat(byte[] bytes, Integer attrOffset, Double attrPrecision) {
        BigInteger tempint = ByteUtils.byteArrToUnsignedInt(bytes);
        //log.info("--------------{},{},{}", tempint, attrOffset, attrPrecision);
        if (null != attrOffset && null != tempint) {
            if (null == attrPrecision) {
                return tempint.subtract(BigInteger.valueOf(attrOffset)).doubleValue();
            } else {
                return BigDecimal.valueOf(attrPrecision).multiply(new BigDecimal(tempint))
                        .subtract(new BigDecimal(attrOffset)).doubleValue();
            }
        }
        return tempint.doubleValue();
    }

    /**
     * 解析时间戳
     *
     * @param bytes
     * @return
     */
    public static Long resolveLong(byte[] bytes) {
        return ByteUtils.byteArrToLong(bytes);
    }

    /**
     * 解析字符串-16进制
     *
     * @param bytes
     * @return
     */
    public static String resolveHexStr(byte[] bytes) {
        return ByteUtils.bytesToHexStr(bytes);
    }

    /**
     * 解析字符串-16进制
     *
     * @param bytes
     * @return
     */
    public static String resolveHexStr(byte[] bytes, int cursor, int length) {
        bytes = Arrays.copyOfRange(bytes, cursor, cursor + length);
        return resolveHexStr(bytes);
    }

    /**
     * 解析字符串-ascii码
     *
     * @param bytes
     * @return
     */
    public static String resolveASCIIStr(byte[] bytes) {
        return ByteUtils.byteToString(bytes, "UTF-8");
    }


    /**
     * 解析电池序列号
     *
     * @param bytes
     * @return
     */
    public static String resolveBatterySn(byte[] bytes) {
        String s = ByteUtils.byteToString(bytes, "UTF-8");
        if (s != null) {
            return new StringBuilder(s).reverse().toString();
        }
        return null;
    }

    /**
     * 加强版解析ascii，当子节数组中包含不可见ascii时则解析成16进制字符串，并加上非法标识
     *
     * @return
     */
    public static String resolveASCIIPro(byte[] dfBytes, int cursor, int length) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, cursor, cursor + length);
        if (ByteUtils.isContainAsciiControl(bytes)) {
            return "invalidascii:" + resolveHexStr(bytes);
        } else {
            return resolveASCIIStr(bytes);
        }
    }

    /**
     * 解析固件版本号和PN-硬件PN长度
     *
     * @param dfBytes
     * @return
     */
    public static int resolvePNLen(byte[] dfBytes, int cursor, int length) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, cursor, cursor + length);
        return resolveInt(bytes);
    }

    /**
     * 解析ASCII码
     *
     * @param dfBytes
     * @return
     */
    public static String resolveASCII(byte[] dfBytes, int cursor, int length) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, cursor, cursor + length);
        return resolveASCIIStr(bytes);
    }

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
     * 解析故障码-经度
     *
     * @return
     */
    public static Double resolveLongitude(byte[] dfBytes, int start, int end) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, start, end);
        Double longitude = resolveFloat(bytes, LONGITUDE_OFFSET, GPS_PRECISION);
        return longitude;
    }

    /**
     * 解析故障码-纬度
     *
     * @return
     */
    public static Double resolveLatitude(byte[] dfBytes, int start, int end) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, start, end);
        Double latitude = resolveFloat(bytes, LATITUDE_OFFSET, GPS_PRECISION);
        return latitude;
    }

    /**
     * 解析最高故障等级
     *
     * @return
     */
    public static String resolveAlarmLevel(byte[] dfBytes, int start, int end) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, start, end);
        String alarmHexStr = ByteUtils.bytesToHexStr(bytes);
        return alarmHexStr;
    }

    /**
     * 解析故障类型标志
     *
     * @return
     */
    public static String resolveFaultCode(byte[] dfBytes) {
        byte[] bytes = Arrays.copyOfRange(dfBytes, 0, 1);
        String faultCodeHexStr = ByteUtils.bytesToHexStr(bytes);
        return faultCodeHexStr;
    }

    /**
     * 解析ip地址
     *
     * @return
     */
    public static String resolveIp(byte[] bytes) {
        long ip = ByteUtils.byteArrToLong(bytes);
        StringBuffer sb = new StringBuffer();
        //直接右移24位
        sb.append(String.valueOf((ip >>> 24))).append(".");
        //将高8位置0，右移16位
        sb.append(String.valueOf((ip & 0X00FFFFFF) >>> 16)).append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((ip & 0X0000FFFF) >>> 8)).append(".");
        //将高24位置0
        sb.append(String.valueOf((ip & 0X000000FF)));
        return sb.toString();
    }

    /**
     * 解析固件版本
     * 2字节
     * 如： 0x100，表示v1.0.0；
     * 0xA10A，表示v10.1.0.10
     *
     * @return
     */
    public static String resolveFirmwareVersion(byte[] bytes) {
        int version = ByteUtils.byteArrToInt(bytes);
        StringBuilder sb = new StringBuilder("v");
        //右移12位
        sb.append(version >>> 12).append(".");
        //右移8位
        sb.append((version >>> 8) & 0x0F).append(".");
        //右移4位置
        sb.append((version >>> 4) & 0x0F).append(".");
        sb.append(version & 0x0F);
        return sb.toString();
    }

    /**
     * 解析电池生产日期
     *
     * @return
     */
    public static String resolveProductionDate(byte[] bytes) {
        short date = ByteUtils.byteArrToShort(bytes);
        if (date != 0) {
            return String.format("20%02d.%02d.%02d",
                    (date >> 9) & 0xFF, (date >> 5) & 0x0F, date & 0x1F);
        }
        return "";
    }

    /**
     * 解析rtc时间
     *
     * @return
     */
    public static String resolveRtcDateTime(byte[] bytes) {
        StringBuffer rtcDateStr = new StringBuffer();
        int year = ByteUtils.getBit(bytes, 0, 5);
        int month = ByteUtils.getBit(bytes, 6, 9);
        int day = ByteUtils.getBit(bytes, 10, 14);
        int hour = ByteUtils.getBit(bytes, 15, 19);
        int minute = ByteUtils.getBit(bytes, 20, 25);
        int second = ByteUtils.getBit(bytes, 26, 31);

        String yearPad = null;
        if (year < 10) {
            yearPad = StringUtils.leftPad(year + "", 2, "0");
        } else {
            yearPad = year + "";
        }

        if (year > currentYear) {
            rtcDateStr.append("19").append(yearPad).append("-");
        } else {
            rtcDateStr.append("20").append(yearPad).append("-");
        }
        rtcDateStr.append(month).append("-");
        rtcDateStr.append(day).append(" ");
        rtcDateStr.append(hour).append(":");
        rtcDateStr.append(minute).append(":");
        rtcDateStr.append(second);

        return rtcDateStr.toString();
    }

}
