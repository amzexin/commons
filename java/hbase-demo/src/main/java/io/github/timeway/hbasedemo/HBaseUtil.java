package io.github.timeway.hbasedemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HBaseUtil
 *
 * @author Zexin Li
 * @date 2023-02-27 16:57
 */
@Slf4j
public class HBaseUtil {

    // region HBaseUtil 初始化与销毁

    private final Connection connection;

    private static Connection createConnection(Configuration conf) {
        try {
            log.info("hbase连接 开始创建");
            Connection connection = ConnectionFactory.createConnection(conf);
            log.info("hbase连接 创建成功");
            return connection;
        } catch (IOException e) {
            log.error("hbase连接 创建失败: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public HBaseUtil(HBaseConfiguration configuration) {
        this.connection = createConnection(configuration.toOrigin());
    }

    public HBaseUtil(Configuration configuration) {
        this.connection = createConnection(configuration);
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null && !connection.isClosed()) {
            log.info("hbase连接 开始关闭");
            connection.close();
            log.info("hbase连接 关闭完成");
        }
        super.finalize();
    }

    // endregion

    // region ddl of namespace

    public void createNamespace(String namespace) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);
            // builder.addConfiguration("user", "test");
            NamespaceDescriptor namespaceDescriptor = builder.build();
            admin.createNamespace(namespaceDescriptor);
        }
    }

    public List<String> listNamespaceName() throws IOException {
        try (Admin admin = connection.getAdmin()) {
            NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
            return Arrays.stream(namespaceDescriptors).map(NamespaceDescriptor::getName).collect(Collectors.toList());
        }
    }

    // endregion

    // region ddl of table

    public List<SimpleTableName> listTableName() throws IOException {
        try (Admin admin = connection.getAdmin()) {
            return Arrays.stream(admin.listTableNames()).map(x -> new SimpleTableName(x.getNamespaceAsString(), x.getQualifierAsString())).collect(Collectors.toList());
        }
    }

    public SimpleTableDescriptor tableDescriptor(String namespace, String tableName) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            List<TableDescriptor> tableDescriptors = admin.listTableDescriptors(Collections.singletonList(tableNameObj(namespace, tableName)));
            if (tableDescriptors.isEmpty()) {
                return null;
            }
            return new SimpleTableDescriptor(tableDescriptors.get(0));
        }
    }

    public SimpleTableDescriptor tableDescriptor(String tableName) throws IOException {
        return tableDescriptor(null, tableName);
    }

    public SimpleTableDescriptor tableDescriptor(SimpleTableName simpleTableName) throws IOException {
        return tableDescriptor(simpleTableName.getNamespace(), simpleTableName.getQualifier());
    }

    public boolean tableExists(String namespace, String tableName) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            return admin.tableExists(tableNameObj(namespace, tableName));
        }
    }

    public boolean tableExists(String tableName) throws IOException {
        return tableExists(null, tableName);
    }

    public boolean tableExists(SimpleTableName simpleTableName) throws IOException {
        return tableExists(simpleTableName.getNamespace(), simpleTableName.getQualifier());
    }

    // endregion

    // region dml

    public void put(SimpleTableName tableName, String row, List<SimpleCell> cells) throws IOException {
        try (Table table = connection.getTable(tableName.toOrigin())) {
            Put put = new Put(Bytes.toBytes(row));
            for (SimpleCell cell : cells) {
                put.addColumn(Bytes.toBytes(cell.getFamily()), Bytes.toBytes(cell.getQualifier()), Bytes.toBytes(cell.getValue()));
            }
            table.put(put);
        }
    }

    public SimpleResult get(SimpleTableName tableName, String row) throws IOException {
        try (Table table = connection.getTable(tableName.toOrigin())) {
            Get get = new Get(Bytes.toBytes(row));
            Result result = table.get(get);
            return new SimpleResult(result);
        }
    }

    public List<SimpleResult> scan(SimpleTableName tableName, String startRow, String endRow) throws IOException {
        try (Table table = connection.getTable(tableName.toOrigin())) {
            Scan scan = new Scan().withStartRow(Bytes.toBytes(startRow)).withStopRow(Bytes.toBytes(endRow));
            ResultScanner scanner = table.getScanner(scan);

            List<SimpleResult> finalResult = new ArrayList<>();
            for (Result result : scanner) {
                finalResult.add(new SimpleResult(result));
            }
            return finalResult;
        }
    }

    public void delete(SimpleTableName tableName, String row) throws IOException {
        try (Table table = connection.getTable(tableName.toOrigin())) {
            Delete delete = new Delete(Bytes.toBytes(row));
            table.delete(delete);
        }
    }

    // endregion

    // region common method

    private static TableName tableNameObj(String namespace, String tableName) {
        TableName result;
        if (namespace == null) {
            result = TableName.valueOf(tableName);
        } else {
            result = TableName.valueOf(namespace, tableName);
        }
        return result;
    }

    // endregion

}
