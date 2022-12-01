// package io.github.amzexin.commons.test.shardingsphere;
//
// import org.apache.shardingsphere.sql.parser.api.CacheOption;
// import org.apache.shardingsphere.sql.parser.api.SQLParserEngine;
// import org.apache.shardingsphere.sql.parser.api.SQLVisitorEngine;
// import org.apache.shardingsphere.sql.parser.core.ParseASTNode;
// import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.pagination.PaginationValueSegment;
// import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.pagination.limit.LimitSegment;
// import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.pagination.limit.ParameterMarkerLimitValueSegment;
// import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
// import org.apache.shardingsphere.sql.parser.sql.dialect.statement.mysql.dml.MySQLSelectStatement;
// import org.junit.Test;
//
// import java.io.IOException;
// import java.util.Optional;
// import java.util.Properties;
//
// /**
//  * SqlParserV5_2_1Test （4.1.1版本暴露的bug在新版本被修复了）
//  *
//  * @author zexin
//  */
// public class SqlParserV5_2_1Test {
//
//     private void testSqlParserSubstringBug(String sql) throws IOException {
//         // 获取语法解析器
//         SQLParserEngine sqlParserEngine = new SQLParserEngine("MySQL", new CacheOption(10, 100));
//
//         // 解析SQL
//         ParseASTNode parseContext = sqlParserEngine.parse(sql, false);
//         SQLStatement sqlStatement = new SQLVisitorEngine("MySQL", "STATEMENT", true, new Properties()).visit(parseContext);
//
//         // 获取解析后的limit模块
//         Optional<LimitSegment> limitSegment = ((MySQLSelectStatement) sqlStatement).getLimit();
//         Optional<PaginationValueSegment> paginationValueSegment = limitSegment.get().getOffset();
//         ParameterMarkerLimitValueSegment parameterMarkerLimitValueSegment = (ParameterMarkerLimitValueSegment) paginationValueSegment.get();
//         int parameterIndex = parameterMarkerLimitValueSegment.getParameterIndex();
//         int expectIndex = sql.length() - sql.replaceAll("\\?", "").length() - 2;
//         String text = String.format("期望Index = %s, 实际Index = %s", expectIndex, parameterIndex);
//         if (expectIndex == parameterIndex) {
//             System.out.println(text);
//         } else {
//             System.err.println(text);
//         }
//     }
//
//     @Test
//     public void testBug() throws IOException {
//         String sql = "select * from t where ? = substring(a, 3) limit ?, ?;";
//         testSqlParserSubstringBug(sql);
//         sql = "select * from t where substring(a, 3) = ? limit ?, ?;";
//         testSqlParserSubstringBug(sql);
//     }
// }
