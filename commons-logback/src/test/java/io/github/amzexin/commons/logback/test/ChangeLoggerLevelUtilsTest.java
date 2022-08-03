package io.github.amzexin.commons.logback.test;

import io.github.amzexin.commons.logback.ChangeLoggerLevelUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChangeLoggerLevelUtilsTest
 *
 * @author zexin
 */
public class ChangeLoggerLevelUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(ChangeLoggerLevelUtilsTest.class);

    @Test
    public void test20220803_2239(){
        log.info("info");
        log.debug("debug");
        ChangeLoggerLevelUtils.setLogLevel("debug");
        System.out.println(ChangeLoggerLevelUtils.getLoggerList());
        log.info("info");
        log.debug("debug");
    }
}
