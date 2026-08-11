package com.sunsun.adminspringboot.common;

import java.util.UUID;

public class TraceContext {

    private static final ThreadLocal<String> TRACE_ID_HOLDER = new ThreadLocal<>();

    /**
     * 设置traceId
     */
    public static void setTraceId(String traceId) {
        TRACE_ID_HOLDER.set(traceId);
    }

    /**
     * 获取traceId，不存在则自动生成
     */
    public static String getTraceId() {
        String traceId = TRACE_ID_HOLDER.get();
        if (traceId == null || traceId.isBlank()) {
            traceId = generateTraceId();
            setTraceId(traceId);
        }
        return traceId;
    }

    /**
     * 清除ThreadLocal（请求结束必须调用，防止线程池串数据）
     */
    public static void clear() {
        TRACE_ID_HOLDER.remove();
    }

    /**
     * 生成traceId
     */
    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}