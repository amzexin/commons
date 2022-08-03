package io.github.amzexin.commons.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态调整日志等级类
 *
 * @author zexin
 */
public class ChangeLoggerLevelUtils {

    private static final Logger log = LoggerFactory.getLogger(ChangeLoggerLevelUtils.class);

    private static final String LOGBACK_LOGGER_FACTORY = "ch.qos.logback.classic.util.ContextSelectorStaticBinder";

    private static final Map<String, ch.qos.logback.classic.Logger> loggerMap = new ConcurrentHashMap<>();

    static {
        String loggerFactoryClassStr = StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr();

        if (!LOGBACK_LOGGER_FACTORY.equals(loggerFactoryClassStr)) {
            throw new RuntimeException("不支持当前Log框架: " + loggerFactoryClassStr);
        }

        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : loggerContext.getLoggerList()) {
            if (logger.getLevel() != null) {
                loggerMap.put(logger.getName(), logger);
            }
        }

        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        loggerMap.put(rootLogger.getName(), rootLogger);

        log.info("所有logger收集完毕. loggers = {}", loggerMap.values());
    }

    private ChangeLoggerLevelUtils() {

    }

    public static void setLogLevel(String logLevel) {
        log.info("设置所有Logger级别为[{}]", logLevel);
        if (loggerMap.isEmpty()) {
            throw new RuntimeException("当前工程中不存在任何Logger, 无法调整Logger级别");
        }

        Set<Map.Entry<String, ch.qos.logback.classic.Logger>> entries = loggerMap.entrySet();
        for (Map.Entry<String, ch.qos.logback.classic.Logger> entry : entries) {
            ch.qos.logback.classic.Logger logger = entry.getValue();
            ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(logLevel);
            logger.setLevel(targetLevel);
        }
    }

    public static void setLogLevel(String loggerName, String loggerLevel) {
        log.info("设置logger: {}, 级别为: {}", loggerName, loggerLevel);
        ch.qos.logback.classic.Logger logger = loggerMap.get(loggerName);
        if (null == logger) {
            throw new RuntimeException("当前工程不存在该logger");
        }

        ch.qos.logback.classic.Level targetLevel = ch.qos.logback.classic.Level.toLevel(loggerLevel);
        logger.setLevel(targetLevel);
    }

    public static String getLoggerList() {
        StringBuilder result = new StringBuilder();
        result.append("{\"logFramework\":\"logback\"").append(",");
        result.append("\"loggerList\": [");
        List<ch.qos.logback.classic.Logger> loggers = new ArrayList<>(loggerMap.values());
        for (int i = 0; i < loggers.size(); i++) {
            ch.qos.logback.classic.Logger logger = loggers.get(i);
            result.append("{");
            result.append("\"loggerName\": \"").append(logger.getName()).append("\",");
            result.append("\"loggerLevel\": \"").append(logger.getLevel().toString()).append("\"");
            result.append("}");
            if (i != loggers.size() - 1) {
                result.append(",");
            }
        }
        result.append("]");
        result.append("}");
        return result.toString();
    }

}
