package io.github.amzexin.commons.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Description: TraceIdUtil
 *
 * @author Lizexin
 * @date 2021-05-19 19:16
 */
public class TraceIdUtils {

    public static final String TRACE_ID_KEY = "trace_id";

    /**
     * 设置traceId
     */
    public static void setupTraceId() {
        setupTraceId(null);
    }

    /**
     * 设置traceId
     */
    public static void setupTraceId(String traceId) {
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 获取traceId
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 清空traceId
     */
    public static void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    private TraceIdUtils() {
    }
}