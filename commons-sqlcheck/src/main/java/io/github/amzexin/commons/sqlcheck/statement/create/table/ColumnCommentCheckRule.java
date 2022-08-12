package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.Report;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;

/**
 * 必须为字段添加注释
 */
public class ColumnCommentCheckRule extends AbstractCreateTableCheckRule {

    @Override
    public Report match(Statement statement) {
        CreateTable createTable = (CreateTable) statement;

        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String value = null;
            List<String> columnSpecs = columnDefinition.getColumnSpecs();
            for (int i = 0; i < columnSpecs.size(); i++) {
                if (!"COMMENT".equalsIgnoreCase(columnSpecs.get(i))) {
                    continue;
                }
                if (i + 1 < columnSpecs.size()) {
                    value = columnSpecs.get(i + 1);
                }
            }
            if (value == null || value.isEmpty()) {
                String columnName = columnDefinition.getColumnName();
                return Report.unPass(String.format("请为字段[%s]添加注释", columnName));
            }
        }

        return Report.pass();
    }
}
