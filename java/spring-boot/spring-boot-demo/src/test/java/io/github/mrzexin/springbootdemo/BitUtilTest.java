package io.github.mrzexin.springbootdemo;

import io.github.mrzexin.springbootdemo.util.BitUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * BitUtilTest
 *
 * @author Zexin Li
 * @date 2023-03-29 15:47
 */
@Slf4j
public class BitUtilTest {

    public void print(String str) {
        log.info("{} -> {}", str, BitUtil.binaryStringToByte(str));
    }

    public void print(Object o) {
        if (o instanceof Byte) {
            log.info("{} -> {}", o, BitUtil.toBinaryString((Byte) o));
        } else if (o instanceof Integer) {
            log.info("{} -> {}", o, BitUtil.toBinaryString((Integer) o));
        }
    }


    @Test
    public void test20230329_1547() {
        String str = "11111111";
        print(str);

        byte b = -118;
        print(b);                       // 10001010
        print((byte) (b >> 1));         // 11000101
        print((byte) (b >>> (byte) 1)); // 11000101

        int i = -99999;
        print(i);      // 11111111111111111111111110001010
        print(i >> 1); // 11111111111111111111111111000101
        print(i >>> 1);// 01111111111111111111111111000101
    }

    @Test
    public void test20230329_1752(){
        String str = "11111111";
        byte b = BitUtil.binaryStringToByte(str);
        System.out.println(BitUtil.toHexString(b));
    }
}
