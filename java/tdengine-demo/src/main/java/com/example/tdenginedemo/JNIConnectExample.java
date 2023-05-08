package com.example.tdenginedemo;

import com.taosdata.jdbc.TSDBDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JNIConnectExample {

    /**
     * 原生连接方式，需要加 -Djava.library.path=/usr/local/lib
     *
     * @throws SQLException
     */
    private static void nativeConnect() throws SQLException {
        String jdbcUrl = "jdbc:TAOS://localhost:6030?user=root&password=taosdata";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
        System.out.println("Connected");
        conn.close();
    }

    /**
     * REST 连接方式
     *
     * @throws SQLException
     */
    public static void restConnect() throws SQLException {
        String jdbcUrl = "jdbc:TAOS-RS://localhost:6041?user=root&password=taosdata";
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_BATCH_LOAD, "true");
        Connection conn = DriverManager.getConnection(jdbcUrl, connProps);
        System.out.println("Connected");
        conn.close();
    }

    public static void main(String[] args) throws SQLException {
        // nativeConnect();
        restConnect();
    }
}

// use
// String jdbcUrl = "jdbc:TAOS://localhost:6030/dbName?user=root&password=taosdata";
// if you want to connect a specified database named "dbName".
