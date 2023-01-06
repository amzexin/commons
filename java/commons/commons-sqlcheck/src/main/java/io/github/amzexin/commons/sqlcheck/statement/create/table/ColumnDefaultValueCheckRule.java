package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.Report;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;

/**
 * 必须为字段添加默认值
 */
public class ColumnDefaultValueCheckRule extends AbstractCreateTableCheckRule {

    @Override
    public Report match(Statement statement) {
        CreateTable createTable = (CreateTable) statement;

        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
        ColumnDefinition primaryKey = primaryKey(createTable);
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            // 主键不检查默认值
            if (primaryKey != null && primaryKey.getColumnName().equalsIgnoreCase(columnDefinition.getColumnName())) {
                continue;
            }

            // 检查是否有默认值
            String value = null;
            List<String> columnSpecs = columnDefinition.getColumnSpecs();
            for (int i = 0; i < columnSpecs.size(); i++) {
                if (!"DEFAULT".equalsIgnoreCase(columnSpecs.get(i))) {
                    continue;
                }
                if (i + 1 < columnSpecs.size()) {
                    value = columnSpecs.get(i + 1);
                }
            }
            if (value == null || value.isEmpty()) {
                String columnName = columnDefinition.getColumnName();
                return Report.unPass(String.format("请为字段[%s]添加默认值", columnName));
            }
        }

        return Report.pass();
    }
}
