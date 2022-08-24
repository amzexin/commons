package io.github.amzexin.commons.test.gc;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * Description: GCTest
 *
 * @author Lizexin
 * @date 2022-08-22 14:42
 */
@Slf4j
public class GCTest {

    private final static int MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[][] bytes = new byte[2][];
        int index = 0;
        int gcCount = 0;
        int maxGcCount = 1;
        while (true) {
            try {
                if (index == bytes.length - 1) {
                    log.info("copy bytes({}MB)", index);
                    bytes = Arrays.copyOf(bytes, bytes.length * 2);
                }
                log.info("{}MB", index);
                bytes[index++] = new byte[MB];
                Thread.sleep(500);
            } catch (OutOfMemoryError e) {
                log.error(e.getMessage(), e);
                gcCount++;
                if (gcCount > maxGcCount) {
                    log.info("gc count > {}, clear memory", maxGcCount);
                    bytes = new byte[2][];
                    index = 0;
                    gcCount = 0;
                }
                System.gc();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
