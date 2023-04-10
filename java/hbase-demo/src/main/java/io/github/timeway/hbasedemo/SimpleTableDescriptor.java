package io.github.timeway.hbasedemo;

import lombok.Data;
import org.apache.hadoop.hbase.client.TableDescriptor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SimpleTableDescriptor
 *
 * @author Zexin Li
 * @date 2023-04-07 19:48
 */
@Data
public class SimpleTableDescriptor {
    private SimpleTableName tableName;
    private List<SimpleColumnFamilyDescriptor> columnFamilies;

    public SimpleTableDescriptor(TableDescriptor tableDescriptor) {
        this.tableName = new SimpleTableName(tableDescriptor.getTableName());
        this.columnFamilies = Arrays.stream(tableDescriptor.getColumnFamilies()).map(SimpleColumnFamilyDescriptor::new).collect(Collectors.toList());
    }
}
