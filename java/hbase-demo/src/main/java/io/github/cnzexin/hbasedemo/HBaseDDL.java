package io.github.cnzexin.hbasedemo;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HBaseDDL
 *
 * @author Zexin Li
 * @date 2023-02-27 16:57
 */
public class HBaseDDL {

    private static Logger logger = LoggerFactory.getLogger(HBaseConnection.class);

    private static Connection connection = HBaseConnection.getConnection();

    public static void createNamespace(String namespace) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);
            // builder.addConfiguration("user", "test");
            NamespaceDescriptor namespaceDescriptor = builder.build();
            admin.createNamespace(namespaceDescriptor);
        }
    }

    public static List<String> listNamespaceName() throws IOException {
        try (Admin admin = connection.getAdmin()) {
            NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
            return Arrays.stream(namespaceDescriptors).map(NamespaceDescriptor::getName).collect(Collectors.toList());
        }
    }

    public static List<SimpleTableName> listTableName() throws IOException {
        try (Admin admin = connection.getAdmin()) {
            return Arrays.stream(admin.listTableNames()).map(x -> new SimpleTableName(x.getNamespaceAsString(), x.getQualifierAsString())).collect(Collectors.toList());
        }
    }

    public static TableDescriptor tableDescriptor(String namespace, String tableName) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            List<TableDescriptor> tableDescriptors = admin.listTableDescriptors(Collections.singletonList(tableNameObj(namespace, tableName)));
            if (tableDescriptors.isEmpty()) {
                return null;
            }
            return tableDescriptors.get(0);
        }
    }

    public static TableDescriptor tableDescriptor(String tableName) throws IOException {
        return tableDescriptor(null, tableName);
    }

    public static TableDescriptor tableDescriptor(SimpleTableName simpleTableName) throws IOException {
        return tableDescriptor(simpleTableName.getNamespace(), simpleTableName.getQualifier());
    }

    public static boolean tableExists(String namespace, String tableName) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            return admin.tableExists(tableNameObj(namespace, tableName));
        }
    }

    public static boolean tableExists(String tableName) throws IOException {
        return tableExists(null, tableName);
    }

    public static boolean tableExists(SimpleTableName simpleTableName) throws IOException {
        return tableExists(simpleTableName.getNamespace(), simpleTableName.getQualifier());
    }

    private static TableName tableNameObj(String namespace, String tableName) {
        TableName result;
        if (namespace == null) {
            result = TableName.valueOf(tableName);
        } else {
            result = TableName.valueOf(namespace, tableName);
        }
        return result;
    }

}
