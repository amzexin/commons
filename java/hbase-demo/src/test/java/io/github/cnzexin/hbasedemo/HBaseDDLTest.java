package io.github.cnzexin.hbasedemo;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * HBaseDDLTest
 *
 * @author Zexin Li
 * @date 2023-02-27 17:26
 */
public class HBaseDDLTest {

    private static final Logger logger = LoggerFactory.getLogger(HBaseConnection.class);

    private SimpleTableName currentTable = new SimpleTableName("test", "test");

    @Test
    public void testListNamespace() throws IOException {
        List<String> namespaces = HBaseDDL.listNamespaceName();
        logger.info("namespaces = {}", namespaces);
    }

    @Test
    public void testCreateNamespace() throws IOException {
        HBaseDDL.createNamespace("test");
    }

    @Test
    public void testListTableName() throws IOException {
        logger.info("{}", JSON.toJSONString(HBaseDDL.listTableName()));
    }

    @Test
    public void testTableDescriptor() throws IOException {
        logger.info("{}", JSON.toJSONString(HBaseDDL.tableDescriptor(currentTable)));
    }

    @Test
    public void testIsTableExists() throws IOException {
        System.out.println(HBaseDDL.tableExists("bigdata", "student"));
        System.out.println(HBaseDDL.tableExists("bigdata", "person"));
    }

}
