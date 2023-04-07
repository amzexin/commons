package io.github.cnzexin.hbasedemo;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}
