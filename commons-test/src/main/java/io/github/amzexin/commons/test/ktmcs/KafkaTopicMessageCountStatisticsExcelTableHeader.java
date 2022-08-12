package io.github.amzexin.commons.test.ktmcs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Description: TableHeader
 *
 * @author Lizexin
 * @date 2022-05-24 18:09
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KafkaTopicMessageCountStatisticsExcelTableHeader {
    /**
     * 该头部标题所在列，从0开始
     */
    private int index;
    /**
     * 该头部标题
     */
    private String headerName;
    /**
     * 该头部所在列的宽，必须是256的倍数；
     * 比如：列宽为10，实际此值应该填2560
     */
    private int columnWidth;
    /**
     * 数据提取器，定义从某对象获取数据的模板
     */
    private DataExtractor dataExtractor;
    /**
     * 该头部标题单元格的风格
     */
    private HeaderStyleCreator headerStyleCreator;
    /**
     * 该头部标题所在列数据的格式
     */
    private DataStyleCreator dataStyleCreator;

    public static interface DataExtractor {
        String run(Object t);
    }

    public static interface HeaderStyleCreator {
        XSSFCellStyle run(XSSFWorkbook workbook);
    }

    public static interface DataStyleCreator {
        XSSFCellStyle run(XSSFWorkbook workbook);
    }
}
