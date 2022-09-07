package io.github.amzexin.commons.script.kafka.messagestatistics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.amzexin.commons.http.HttpParams;
import io.github.amzexin.commons.http.HttpResult;
import io.github.amzexin.commons.http.HttpUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Description: KafkaTopic近一周消息量统计，最终形成Excel
 *
 * @author Lizexin
 * @date 2022-05-24 15:47
 */
@Slf4j
public class MessageCountStatistician {

    /**
     * kafkaEagle域名
     */
    private static String kafkaEagleDomain = "http://xxx.cn";

    /**
     * 统计开始时间
     */
    private static String stime = "20220425";

    /**
     * 统计终止时间
     */
    private static String etime = "20220524";

    /**
     * token
     */
    private static String cookie = "xxx";

    private static List<MessageCountStatisticsExcelTableHeader> excelTableHeaders = new ArrayList<>(Arrays.asList(
            new MessageCountStatisticsExcelTableHeader(0, "topic", 52 * 256,
                    obj -> {
                        MessageCount messageCount = (MessageCount) obj;
                        return messageCount.getTopic();
                    },
                    workbook -> {
                        XSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        return cellStyle;
                    },
                    workbook -> {
                        XSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.LEFT);
                        return cellStyle;
                    }
            ),
            new MessageCountStatisticsExcelTableHeader(1, "日期", 12 * 256,
                    obj -> {
                        MessageCount messageCount = (MessageCount) obj;
                        return messageCount.getX();
                    },
                    workbook -> {
                        XSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        return cellStyle;
                    },
                    workbook -> {
                        XSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        return cellStyle;
                    }
            ),
            new MessageCountStatisticsExcelTableHeader(2, "消息量", 12 * 256,
                    obj -> {
                        MessageCount messageCount = (MessageCount) obj;
                        return messageCount.y;
                    },
                    workbook -> {
                        XSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        return cellStyle;
                    },
                    workbook -> {
                        XSSFCellStyle cellStyle = workbook.createCellStyle();
                        cellStyle.setAlignment(HorizontalAlignment.CENTER);
                        return cellStyle;
                    }
            )
    ));

    /**
     * 获取所有topic
     *
     * @return
     */
    public List<String> topics() {
        String urlFormat = "%s/topic/mock/list/ajax?page=1&offset=10";
        String url = String.format(urlFormat, kafkaEagleDomain);
        HttpParams httpParams = new HttpParams();
        String[] cookieArr = cookie.split("=");
        httpParams.setCookie(cookieArr[0], cookieArr[1]);

        HttpResult<String> httpResult = HttpUtils.get(url, httpParams);
        if (!httpResult.successful()) {
            return Collections.emptyList();
        }

        String httpResultData = httpResult.getData();
        JSONObject httpResultDataObj = JSON.parseObject(httpResultData);
        JSONArray items = httpResultDataObj.getJSONArray("items");
        List<String> topics = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            topics.add(items.getJSONObject(i).getString("text"));
        }

        return topics;
    }

    /**
     * 获取topic的消息量
     *
     * @param topic
     * @return
     */
    public List<MessageCount> messageCounts(String topic) {
        String urlFormat = "%s/topic/list/filter/select/ajax?stime=%s&etime=%s&topics=%s";
        String url = String.format(urlFormat, kafkaEagleDomain, stime, etime, topic);
        HttpParams httpParams = new HttpParams();
        String[] cookieArr = cookie.split("=");
        httpParams.setCookie(cookieArr[0], cookieArr[1]);

        HttpResult<String> httpResult = HttpUtils.get(url, httpParams);
        if (!httpResult.successful()) {
            return Collections.emptyList();
        }

        try {
            String httpResultData = httpResult.getData();
            List<MessageCount> messageCounts = JSON.parseArray(httpResultData, MessageCount.class);
            messageCounts.stream().forEach(new Consumer<MessageCount>() {
                @Override
                public void accept(MessageCount messageCount) {
                    messageCount.setTopic(topic);

                    // 检查date是否为yyyy-MM-dd，如果是MM-dd则自动以stime的yyyy为准并补齐
                    String date = messageCount.getX();
                    if (date.split("-").length != 3) {
                        messageCount.setX(stime.substring(0, 4) + "-" + date);
                    }
                }
            });
            return messageCounts;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 导出数据
     *
     * @param messageCounts
     * @param outputStream
     * @throws IOException
     */
    public void dataExport(List<MessageCount> messageCounts, OutputStream outputStream) throws IOException {
        // 1. 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 2. 创建工作表
        XSSFSheet sheet = workbook.createSheet();

        // 3. 创建标头所在行
        XSSFRow headerRow = sheet.createRow(0);

        for (int i = 0; i < excelTableHeaders.size(); i++) {
            MessageCountStatisticsExcelTableHeader excelTableHeader = excelTableHeaders.get(i);
            // 3.1 设置每列宽度
            sheet.setColumnWidth(i, excelTableHeader.getColumnWidth());

            // 3.2 设置标头内容与样式
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(excelTableHeader.getHeaderName());
            cell.setCellStyle(excelTableHeader.getHeaderStyleCreator().run(workbook));
        }

        // 4. 填充具体数据
        AtomicInteger currentRowIndex = new AtomicInteger(1);
        for (int i = 0; i < messageCounts.size(); i++) {
            MessageCount messageCount = messageCounts.get(i);

            // 4.1 创建行
            int rowIndex = currentRowIndex.getAndIncrement();
            XSSFRow dataRow = sheet.createRow(rowIndex);

            for (int j = 0; j < excelTableHeaders.size(); j++) {
                // 4.2 创建单元格
                XSSFCell cell = dataRow.createCell(j);

                // 4.3 填充具体的数据
                MessageCountStatisticsExcelTableHeader excelTableHeader = excelTableHeaders.get(j);
                cell.setCellValue(excelTableHeader.getDataExtractor().run(messageCount));
                cell.setCellStyle(excelTableHeader.getDataStyleCreator().run(workbook));
            }

        }

        // 写入
        workbook.write(outputStream);
        outputStream.flush();

        // 释放资源
        outputStream.close();
        workbook.close();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MessageCount {
        /**
         * kafkaTopic
         */
        private String topic;
        /**
         * 日期
         */
        private String x;
        /**
         * 消息量
         */
        private String y;
    }
}
