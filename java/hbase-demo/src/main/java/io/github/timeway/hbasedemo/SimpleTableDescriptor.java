package io.github.timeway.hbasedemo;

import lombok.Data;

import java.util.List;

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
}
