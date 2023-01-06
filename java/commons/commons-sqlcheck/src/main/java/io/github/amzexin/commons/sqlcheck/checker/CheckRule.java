package io.github.amzexin.commons.sqlcheck.checker;

import io.github.amzexin.commons.sqlcheck.statement.StatementTypeEnum;
import net.sf.jsqlparser.statement.Statement;

/**
 * 具体的检查规则
 */
public interface CheckRule {
    /**
     * 检查SQL语句
     *
     * @param statement SQL语句
     * @return 规则检查报告
     */
    Report match(Statement statement);

    /**
     * 该规则支持的SQL语句类型
     *
     * @return SQL语句类型
     */
    StatementTypeEnum statementType();
}
