package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.Report;
import io.github.amzexin.commons.sqlcheck.util.StringUtils;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

/**
 * 必须添加表注释
 */
public class TableCommentCheckRule extends AbstractCreateTableCheckRule {
    @Override
    public Report match(Statement statement) {
        CreateTable createTable = (CreateTable) statement;

        String comment = tableOptionValue(createTable, "COMMENT");

        return (StringUtils.isEmpty(comment)) ? Report.unPass("请添加表注释") : Report.pass();
    }
}
