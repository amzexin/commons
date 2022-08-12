package io.github.amzexin.commons.sqlcheck.unittest;

import io.github.amzexin.commons.sqlcheck.SQLChecker;
import io.github.amzexin.commons.sqlcheck.SQLCheckerException;
import io.github.amzexin.commons.sqlcheck.checker.Checker;
import io.github.amzexin.commons.sqlcheck.checker.Report;
import net.sf.jsqlparser.JSQLParserException;

public class BaseStatementTest {

    public void test(String sql) throws SQLCheckerException, JSQLParserException {
        Report report = SQLChecker.check(sql);
        System.out.println(report);
    }

    public void test(Checker checker, String sql) throws SQLCheckerException, JSQLParserException {
        Report report = SQLChecker.check(checker, sql);
        System.out.println(report);
    }
}
