package io.github.amzexin.commons.test.shardingsphere;

import org.apache.shardingsphere.sql.parser.SQLParserEngine;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.pagination.PaginationValueSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.pagination.limit.LimitSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.pagination.limit.ParameterMarkerLimitValueSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.statement.dml.SelectStatement;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * SqlParserV4_1_1Test （用于复现bug）
 *
 * @author zexin
 */
public class SqlParserV4_1_1Test {

    private void testSqlParserSubstringBug(String sql) {
        // 获取语法解析器
        SQLParserEngine sqlParserEngine = new SQLParserEngine("MySQL");

        // 解析SQL
        SQLStatement sqlStatement = sqlParserEngine.parse(sql, false);

        // 获取解析后的limit模块
        Optional<LimitSegment> limitSegment = ((SelectStatement) sqlStatement).getLimit();
        Optional<PaginationValueSegment> paginationValueSegment = limitSegment.get().getOffset();
        ParameterMarkerLimitValueSegment parameterMarkerLimitValueSegment = (ParameterMarkerLimitValueSegment) paginationValueSegment.get();
        int parameterIndex = parameterMarkerLimitValueSegment.getParameterIndex();
        int expectIndex = sql.length() - sql.replaceAll("\\?", "").length() - 2;
        String text = String.format("期望Index = %s, 实际Index = %s", expectIndex, parameterIndex);
        if (expectIndex == parameterIndex) {
            System.out.println(text);
        } else {
            System.err.println(text);
        }
    }

    @Test
    public void testBug() throws IOException {
        String sql = "select * from t where ? = substring(a, 3) limit ?, ?;";
        testSqlParserSubstringBug(sql);

        sql = "select * from t where substring(a, 3) = ? limit ?, ?;";
        testSqlParserSubstringBug(sql);
    }
}
