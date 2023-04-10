package io.github.timeway.hbasedemo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * HBaseDDLTest
 *
 * @author Zexin Li
 * @date 2023-02-27 17:26
 */
@Slf4j
public class HBaseUtilTest {

    private Configuration testConf;

    private HBaseUtil hBaseUtil;

    public static final String test_namespace = "test.namespace";

    public static final String test_qualifier = "test.qualifier";

    public static final String test_rowkey_start = "test.rowkey.start";

    public static final String test_rowkey_end = "test.rowkey.end";

    public static final String test_rowkey = "test.rowkey";

    private SimpleTableName getTableName() {
        return new SimpleTableName(testConf.get(test_namespace), testConf.get(test_qualifier));
    }

    private String getStartRow() {
        return testConf.get(test_rowkey_start);
    }

    private String getEndRow() {
        return testConf.get(test_rowkey_end);
    }

    private String getRow() {
        return testConf.get(test_rowkey);
    }

    @Before
    public void before() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("logs/hbase-site.xml"));
        Configuration conf = new Configuration();
        conf.addResource(is);

        this.testConf = conf;
        this.hBaseUtil = new HBaseUtil(conf);
    }

    @Test
    public void testListNamespace() throws IOException {
        List<String> namespaces = hBaseUtil.listNamespaceName();
        log.info("namespaces = {}", namespaces);
    }

    @Test
    public void testCreateNamespace() throws IOException {
        hBaseUtil.createNamespace("test");
    }

    @Test
    public void testListTableName() throws IOException {
        log.info("{}", JSON.toJSONString(hBaseUtil.listTableName()));
    }

    @Test
    public void testTableDescriptor() throws IOException {
        log.info("{}", JSON.toJSONString(hBaseUtil.tableDescriptor(getTableName())));
    }

    @Test
    public void testIsTableExists() throws IOException {
        System.out.println(hBaseUtil.tableExists("bigdata", "student"));
        System.out.println(hBaseUtil.tableExists("bigdata", "person"));
    }

    @Test
    public void testGet() throws IOException {
        SimpleResult simpleResult = hBaseUtil.get(getTableName(), getRow());
        log.info("simpleResult = {}", JSON.toJSONString(simpleResult));
    }

    @Test
    public void testPut() throws IOException {
        String row = "test";
        ArrayList<SimpleCell> cells = new ArrayList<>();
        SimpleCell simpleCell = new SimpleCell();
        simpleCell.setFamily("test");
        simpleCell.setQualifier("test");
        simpleCell.setValue("test");
        cells.add(simpleCell);
        hBaseUtil.put(getTableName(), row, cells);
    }

    @Test
    public void testScan() throws IOException {
        List<SimpleResult> result = hBaseUtil.scan(getTableName(), getStartRow(), getEndRow());
        log.info("result = {}", JSON.toJSONString(result));
    }

    @Test
    public void testDelete() throws IOException {
        String row = "test";
        hBaseUtil.delete(getTableName(), row);
    }

}
