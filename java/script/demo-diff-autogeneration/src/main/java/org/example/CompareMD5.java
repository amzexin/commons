package org.example;

import cn.hutool.crypto.digest.MD5;

import java.io.File;

/**
 * CompareMD5
 *
 * @author Zexin Li
 * @date 2023-03-07 17:03
 */
public class CompareMD5 {

    public static void main(String[] args) {
        String oldFile = "/Users/lizexin/Downloads/Mmax_ECU_DIFF_427_V4.2.8.bin";
        String newFile = "/Users/lizexin/Downloads/427-428-1.bin";
        MD5 md5 = MD5.create();
        String oldFileMd5 = md5.digestHex(new File(oldFile));
        String newFileMd5 = md5.digestHex(new File(newFile));
        System.out.println("oldFileMd5: " + oldFileMd5);
        System.out.println("newFileMd5: " + newFileMd5);
        System.out.println("是否一致: " + oldFileMd5.equals(newFileMd5));
    }
}
