package io.github.amzexin.commons.test.jvm.gc;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * Description: GCTest
 *
 * @author Lizexin
 * @date 2022-08-22 14:42
 */
@Slf4j
public class GCTest {

    private final static int MB = 1024 * 1024;

    private static void oomTest(){
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

    private static void javaCommandTest() throws InterruptedException {
        byte[] bytes = new byte[500 * MB];
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    public static void main(String[] args) throws InterruptedException {
        javaCommandTest();
    }

}
