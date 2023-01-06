package io.github.amzexin.commons.sqlcheck.statement;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;

public enum StatementTypeEnum {
    CREATE_TABLE,
    ;

    public static StatementTypeEnum getByStatement(Statement statement) {
        if (statement instanceof CreateTable) {
            return CREATE_TABLE;
        }
        return null;
    }
}
