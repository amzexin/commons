package io.github.mrzexin.springbootdemo;


import io.github.mrzexin.springbootdemo.util.ByteUtils;
import io.github.mrzexin.springbootdemo.util.ResolveUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * ResolveUtilTestt
 *
 * @author Zexin Li
 * @date 2023-03-27 17:07
 */
@Slf4j
public class ResolveUtilTest {

    @Test
    public void testResolveExASCII() {
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

        System.out.println(ResolveUtil.resolveExASCII(bytes, 0, bytes.length));
    }

    @Test
    public void testResolveInt() {
        byte[] bytes = {-1};
        System.out.println(ResolveUtil.resolveInt(bytes));
    }

    /**
     * 位运算总结：
     * 正数：
     * 二进制为 原码
     * 左移右边补0；右移左边补0；
     * 负数：
     * 二进制为 补码
     * 左移右边补0；右移左边补1；无符号右移左边补0；
     */
    @Test
    public void testBitMove() {
        byte x = 1;
        log.info("{}的二进制{}", x, ByteUtils.byteToBinaryStr(x));
        for (byte i = 0; i < 8; i++) {
            log.info("{}左移{}: {}", x, i, (x << i));
        }
        x = -127;
        log.info("{}的二进制{}", x, ByteUtils.byteToBinaryStr(x));
        for (byte i = 0; i < 8; i++) {
            log.info("{}右移{}: {}", x, i, x >> i);
        }
    }

    @Test
    public void test20230413_1721() {
        String firmwareVersion = ResolveUtil.resolveFirmwareVersion(new byte[]{0x0A, 0x04});
        System.out.println(firmwareVersion);
    }
}
