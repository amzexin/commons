package io.github.timeway.hbasedemo;

import lombok.Data;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SimpleRow
 *
 * @author Zexin Li
 * @date 2023-04-10 14:40
 */
@Data
public class SimpleResult {
    /**
     * row key
     */
    private String row;
    /**
     * 行数据
     * Map<ColumnFamily, Map<ColumnQualifier, ColumnValue>>
     */
    private Map<String, Map<String, String>> cells;

    public SimpleResult(Result _result) {
        this.row = new String(_result.getRow());
        this.cells = new LinkedHashMap<>();
        for (Cell _cell : _result.rawCells()) {
            SimpleCell simpleCell = new SimpleCell(_cell);
            Map<String, String> data = cells.computeIfAbsent(simpleCell.getFamily(), s -> new LinkedHashMap<>());
            data.put(simpleCell.getQualifier(), simpleCell.getValue());
        }
    }
}
