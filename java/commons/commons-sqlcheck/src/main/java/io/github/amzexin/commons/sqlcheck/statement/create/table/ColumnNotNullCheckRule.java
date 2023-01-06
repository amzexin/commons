package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.Report;
import io.github.amzexin.commons.sqlcheck.util.StringUtils;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.Arrays;
import java.util.List;

/**
 * 字段必须添加NOT NULL
 */
public class ColumnNotNullCheckRule extends AbstractCreateTableCheckRule {

    private static final List<String> NOT_NULL = Arrays.asList("NOT", "NULL");

    @Override
    public Report match(Statement statement) {
        CreateTable createTable = (CreateTable) statement;

        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            List<String> columnSpecs = columnDefinition.getColumnSpecs();
            boolean notNull = StringUtils.containsList(columnSpecs, NOT_NULL);
            if (!notNull) {
                String columnName = columnDefinition.getColumnName();
                return Report.unPass(String.format("请为字段[%s]添加NOT NULL", columnName));
            }
        }

        return Report.pass();
    }
}
