package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.CheckRule;
import io.github.amzexin.commons.sqlcheck.statement.StatementTypeEnum;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;

public abstract class AbstractCreateTableCheckRule implements CheckRule {

    @Override
    public StatementTypeEnum statementType() {
        return StatementTypeEnum.CREATE_TABLE;
    }

    protected ColumnDefinition primaryKey(CreateTable createTable) {
        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            List<String> columnSpecs = columnDefinition.getColumnSpecs();
            for (String columnSpec : columnSpecs) {
                if ("PRIMARY".equalsIgnoreCase(columnSpec)) {
                    return columnDefinition;
                }
            }
        }
        return null;
    }

    protected ColumnDefinition columnDefinition(CreateTable createTable, String columnName) {
        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (columnName.equalsIgnoreCase(columnDefinition.getColumnName())) {
                return columnDefinition;
            }
        }
        return null;
    }

    protected String tableOptionValue(CreateTable createTable, String optionKey) {
        List<String> tableOptionsStrings = createTable.getTableOptionsStrings();
        for (int i = 0; i < tableOptionsStrings.size(); i++) {
            if (optionKey.equalsIgnoreCase(tableOptionsStrings.get(i))) {
                if (i + 1 < tableOptionsStrings.size()) {
                    if ("=".equals(tableOptionsStrings.get(i + 1)) && i + 2 < tableOptionsStrings.size()) {
                        return tableOptionsStrings.get(i + 2);
                    } else {
                        return tableOptionsStrings.get(i + 1);
                    }
                }
            }
        }
        return null;
    }
}
