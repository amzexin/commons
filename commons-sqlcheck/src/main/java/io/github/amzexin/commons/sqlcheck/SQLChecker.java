package io.github.amzexin.commons.sqlcheck;

import io.github.amzexin.commons.sqlcheck.checker.CheckRule;
import io.github.amzexin.commons.sqlcheck.checker.Checker;
import io.github.amzexin.commons.sqlcheck.checker.Report;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.util.ServiceLoader;

/**
 * Description: SQLChecker
 *
 * @author Lizexin
 * @date 2021-11-05 17:05
 */
public class SQLChecker {

    private static final Checker DEFAULT_CHECKER = new Checker();

    static {
        ServiceLoader<CheckRule> services = ServiceLoader.load(CheckRule.class);
        for (CheckRule rule : services) {
            DEFAULT_CHECKER.registeRule(rule);
        }
    }

    public static Report check(String sql) throws JSQLParserException, SQLCheckerException {
        return check(DEFAULT_CHECKER, sql);
    }

    public static Report check(Checker checker, String sql) throws JSQLParserException, SQLCheckerException {
        Statement statement = CCJSqlParserUtil.parse(sql);

        return checker.check(statement);
    }

    private SQLChecker() {
    }
}
