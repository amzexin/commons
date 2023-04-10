package io.github.timeway.hbasedemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.hadoop.hbase.TableName;

/**
 * SimpleTableName
 *
 * @author Zexin Li
 * @date 2023-04-07 17:36
 */
@AllArgsConstructor
@Data
public class SimpleTableName {
    private String namespace;
    private String qualifier;

    public SimpleTableName(TableName tableName) {
        this.namespace = tableName.getNamespaceAsString();
        this.qualifier = tableName.getQualifierAsString();
    }

    public TableName toOrigin() {
        return TableName.valueOf(namespace, qualifier);
    }
}
