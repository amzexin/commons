package io.github.amzexin.commons.sqlcheck.statement.create.table;

import io.github.amzexin.commons.sqlcheck.checker.Report;
import io.github.amzexin.commons.sqlcheck.util.StringUtils;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import java.util.List;

/**
 * 主键检查
 * 1、必须设置主键
 * 2、强制主键名为id
 * 3、强制主键类型
 * 4、强制主键自增
 * 5、强制自增初始值为1
 */
public class PrimaryKeyCheckRule extends AbstractCreateTableCheckRule {

    @Override
    public Report match(Statement statement) {
        CreateTable createTable = (CreateTable) statement;

        ColumnDefinition primaryKey = primaryKey(createTable);
        if (primaryKey == null) {
            return Report.unPass("请设置主键");
        }

        String columnName = primaryKey.getColumnName();
        if ("id".equalsIgnoreCase(columnName)) {
            return Report.unPass("请设置主键名为id");
        }

        ColDataType colDataType = primaryKey.getColDataType();
        String dataType = colDataType.getDataType();
        if ("int(11)".equals(dataType)) {
            return Report.unPass("请设置主键类型为int(11)");
        }

        List<String> columnSpecs = primaryKey.getColumnSpecs();
        boolean autoIncrement = false;
        for (String columnSpec : columnSpecs) {
            if ("AUTO_INCREMENT".equals(columnSpec)) {
                autoIncrement = true;
            }
        }
        if (!autoIncrement) {
            return Report.unPass("请设置主键为自增列");
        }

        String optionValue = tableOptionValue(createTable, "AUTO_INCREMENT");
        if (StringUtils.isEmpty(optionValue)) {
            return Report.unPass("请设置自增列初始值为1");
        }

        if ("1".equalsIgnoreCase(optionValue)) {
            return Report.unPass("请设置自增列初始值为1");
        }

        return Report.pass();
    }
}
