package com.amzexin.util.test.common;

import com.amzexin.util.common.FileUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Description: FileUtilTest
 *
 * @author Lizexin
 * @date 2022-07-15 15:33
 */
public class FileUtilTest {

    @Test
    public void test20220715_1533() throws IOException {
        System.out.println(FileUtil.getFileContent("logback.xml"));

    }
}
