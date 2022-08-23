package io.github.amzexin.commons.test.gc;

import java.util.Arrays;

/**
 * Description: GCTest
 *
 * @author Lizexin
 * @date 2022-08-22 14:42
 */
public class GCTest {

    private final static int MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[][] bytes = new byte[2][];
        int index = 0;
        int gcCount = 0;
        while (true) {
            try {
                if (index == bytes.length - 1) {
                    System.out.println(String.format("copy bytes(%sMB)", index));
                    bytes = Arrays.copyOf(bytes, bytes.length * 2);
                }
                System.out.println(String.format("%sMB", index));
                bytes[index++] = new byte[MB];
                Thread.sleep(500);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                gcCount++;
                if (gcCount > 5) {
                    System.out.println("gc count > 5, clear memory");
                    bytes = new byte[2][];
                    index = 0;
                    gcCount = 0;
                }
                System.gc();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

}
