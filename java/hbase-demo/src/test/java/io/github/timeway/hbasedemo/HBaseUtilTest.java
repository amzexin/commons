package io.github.timeway.hbasedemo;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * HBaseDDLTest
 *
 * @author Zexin Li
 * @date 2023-02-27 17:26
 */
public class HBaseUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(HBaseUtilTest.class);

    private SimpleTableName currentTable;

    private HBaseUtil hBaseUtil;

    @Before
    public void before() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("logs/hbase-site.xml"));
        Configuration conf = new Configuration();
        conf.addResource(is);

        currentTable = new SimpleTableName(conf.get(PropertyName.test_namespace), conf.get(PropertyName.test_qualifier));
        hBaseUtil = new HBaseUtil(conf);
    }

    @Test
    public void testListNamespace() throws IOException {
        List<String> namespaces = hBaseUtil.listNamespaceName();
        logger.info("namespaces = {}", namespaces);
    }

    @Test
    public void testCreateNamespace() throws IOException {
        hBaseUtil.createNamespace("test");
    }

    @Test
    public void testListTableName() throws IOException {
        logger.info("{}", JSON.toJSONString(hBaseUtil.listTableName()));
    }

    @Test
    public void testTableDescriptor() throws IOException {
        logger.info("{}", JSON.toJSONString(hBaseUtil.tableDescriptor(currentTable)));
    }

    @Test
    public void testIsTableExists() throws IOException {
        System.out.println(hBaseUtil.tableExists("bigdata", "student"));
        System.out.println(hBaseUtil.tableExists("bigdata", "person"));
    }

}
