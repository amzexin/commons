package io.github.amzexin.commons.script.kafka.messagestatistics;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: MessageCountStatisticianTest
 *
 * @author Lizexin
 * @date 2022-09-08 11:35
 */
@Slf4j
public class MessageCountStatisticianTest {

    private MessageCountStatistician messageCountStatistician = new MessageCountStatistician();

    @Test
    public void mainTest() throws IOException {
        // 获取所有Topic
        List<String> topicList = messageCountStatistician.topics();

        // 获取所有topic的消息量
        List<MessageCountStatistician.MessageCount> allMessageCount = new ArrayList<>();
        for (String topic : topicList) {
            allMessageCount.addAll(messageCountStatistician.messageCounts(topic));
        }

        // 导出
        messageCountStatistician.dataExport(allMessageCount, new FileOutputStream("logs/kafka消息量统计.xlsx"));
    }

    @Test
    public void topicsTest() {
        List<String> topicList = messageCountStatistician.topics();
        log.info("topicList = {}", topicList);
    }

    @Test
    public void dataExportTest() throws IOException {
        List<MessageCountStatistician.MessageCount> allMessageCount = Collections.singletonList(new MessageCountStatistician.MessageCount("io/github/amzexin/commons/test/all", "2022-05-24", "10000"));
        messageCountStatistician.dataExport(allMessageCount, new FileOutputStream("logs/kafka消息量统计.xlsx"));
    }
}
