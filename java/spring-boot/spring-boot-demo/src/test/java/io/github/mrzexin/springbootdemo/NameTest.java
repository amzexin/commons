package io.github.mrzexin.springbootdemo;

import java.io.IOException;
import java.util.Scanner;

/**
 * NameTest
 *
 * @author Zexin Li
 * @date 2023-02-04 10:25
 */
public class NameTest {

    public static void main(String[] args) throws IOException {
        int a = 'a';
        int z = 'z';
        for (int i = a; i <= z; i++) {
            for (int j = a; j <= z; j++) {
                System.out.print(String.format("zexin%s%s", (char) i, (char) j));
                System.in.read();
                System.out.print(String.format("%s%szexin", (char) i, (char) j));
                System.in.read();
            }
            System.out.println();
        }
    }
    /**
     * cczexin
     * cnzexin
     * dzzexin
     */
}
