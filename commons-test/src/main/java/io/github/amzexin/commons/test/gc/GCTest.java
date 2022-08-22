package io.github.amzexin.commons.test.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: GCTest
 *
 * @author Lizexin
 * @date 2022-08-22 14:42
 */
public class GCTest {

    private final static int MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        List<MBClazz> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            System.out.println("current " + list.size() + "MB");
            list.add(new MBClazz(1));
            System.gc();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class MBClazz {
        private byte[] mb;

        public MBClazz(int mb) {
            this.mb = new byte[MB * mb];
        }
    }
}
