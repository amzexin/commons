package io.github.amzexin.commons.sqlcheck.unittest;

import org.junit.Test;

/**
 * Description: SelectCheckerTest
 *
 * @author Lizexin
 * @date 2021-11-05 15:45
 */
public class SelectCheckerTest extends BaseStatementTest {

    @Test
    public void WriteClearlySelectFieldRuleTest() throws Exception {
        String sql = "select * from test";
        test(sql);
    }

    @Test
    public void test() throws Exception {
        String sql = "select * from test where id in (select * from test where name = 'zexin321')";
        test(sql);
    }

}
