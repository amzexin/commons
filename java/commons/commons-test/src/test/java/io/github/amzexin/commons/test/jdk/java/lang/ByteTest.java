package io.github.amzexin.commons.test.jdk.java.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Description: ByteTest
 *
 * @author Lizexin
 * @date 2022-09-22 16:05
 */
@Slf4j
public class ByteTest {

    public static final byte INITIAL_FLAG = 0b0001;
    public static final byte PREPARE_COMMIT_FLAG = 0b0010;
    public static final byte COMMITTING_FLAG = 0b0100;
    public static final byte COMMITTED_FLAG = 0b1000;

    @Test
    public void test20220922_1605(){
        System.out.println(INITIAL_FLAG);
        System.out.println(PREPARE_COMMIT_FLAG);
        System.out.println(COMMITTING_FLAG);
        System.out.println(COMMITTED_FLAG);
    }
}
