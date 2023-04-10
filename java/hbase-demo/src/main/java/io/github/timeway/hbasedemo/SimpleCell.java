package io.github.timeway.hbasedemo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;

/**
 * SimpleCell
 *
 * @author Zexin Li
 * @date 2023-04-10 14:13
 */
@NoArgsConstructor
@Data
public class SimpleCell {
    /**
     * row key
     */
    private String row;
    /**
     * 列族
     */
    private String family;
    /**
     * 列名
     */
    private String qualifier;
    /**
     * 列值
     */
    private String value;

    public SimpleCell(Cell cell) {
        this.row = new String(CellUtil.cloneRow(cell));
        this.family = new String(CellUtil.cloneFamily(cell));
        this.qualifier = new String(CellUtil.cloneQualifier(cell));
        this.value = new String(CellUtil.cloneValue(cell));
    }
}
