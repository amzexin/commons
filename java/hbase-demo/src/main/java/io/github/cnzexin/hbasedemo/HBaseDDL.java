package io.github.cnzexin.hbasedemo;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
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

    public static List<String> listNamespace() throws IOException {
        try (Admin admin = connection.getAdmin()) {
            NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
            return Arrays.stream(namespaceDescriptors).map(NamespaceDescriptor::getName).collect(Collectors.toList());
        }
    }

    public static List<String> listTable() throws IOException {
        try (Admin admin = connection.getAdmin()) {
            return admin.listTableDescriptors().stream().map(x -> x.getTableName().getNameWithNamespaceInclAsString()).collect(Collectors.toList());
        }
    }

    public static boolean isTableExists(String namespace, String tableName) throws IOException {
        try (Admin admin = connection.getAdmin()) {
            return admin.tableExists(TableName.valueOf(namespace, tableName));
        }
    }

}
