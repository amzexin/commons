package io.github.amzexin.commons.sqlcheck.unittest;

import io.github.amzexin.commons.sqlcheck.SQLCheckerException;
import net.sf.jsqlparser.JSQLParserException;
import org.junit.Test;

/**
 * Description: CreateTableCheckerTest
 *
 * @author Lizexin
 * @date 2021-11-05 15:45
 */
public class CreateTableCheckerTest extends BaseStatementTest {

    @Test
    public void test() throws SQLCheckerException, JSQLParserException {
        String sql = "CREATE TABLE `test` (`id` int(11) AUTO_INCREMENT primary key comment '主键') AUTO_INCREMENT=3 comment 'test' engine=innodb";
        test(sql);
    }

}
