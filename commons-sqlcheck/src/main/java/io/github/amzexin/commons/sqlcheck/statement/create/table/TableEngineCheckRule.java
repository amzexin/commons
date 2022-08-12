package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.Report;
import io.github.amzexin.commons.sqlcheck.util.StringUtils;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

/**
 * 存储引擎必须为InnoDB
 */
public class TableEngineCheckRule extends AbstractCreateTableCheckRule {

    @Override
    public Report match(Statement statement) {
        CreateTable createTable = (CreateTable) statement;

        String engine = tableOptionValue(createTable, "ENGINE");

        if (StringUtils.isEmpty(engine)) {
            return Report.unPass("请将表存储引擎设置为InnoDB");
        }

        if (!"InnoDB".equalsIgnoreCase(engine)) {
            return Report.unPass("请将表存储引擎设置为InnoDB");
        }

        return Report.pass();
    }
}
