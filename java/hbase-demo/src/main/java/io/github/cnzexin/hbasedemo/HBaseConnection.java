package io.github.cnzexin.hbasedemo;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HBaseConnection
 *
 * @author Zexin Li
 * @date 2023-02-27 16:11
 */
class HBaseConnection {

    private static Logger logger = LoggerFactory.getLogger(HBaseConnection.class);

    private static Connection connection = null;

    static {
        try {
            logger.info("hbase连接 开始创建");
            connection = ConnectionFactory.createConnection();
            logger.info("hbase连接 创建成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                closeConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() throws IOException {
        if (!connection.isClosed()) {
            logger.info("hbase连接 开始关闭");
            connection.close();
            logger.info("hbase连接 关闭完成");
        }
    }


}
