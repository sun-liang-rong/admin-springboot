package com.sunsun.adminspringboot.common;

import lombok.Data;

import java.time.Instant;
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private String traceId;
    private Instant timestamp;
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(data);
        response.setMessage("操作成功！");
        response.setTimestamp(Instant.now());
//        response.setTraceId(traceId);
        return response;
    }
//    public static <T> ApiResponse<T> success(T data, String traceId) {
//        ApiResponse<T> response = new ApiResponse<>();
//        response.setCode(200);
//        response.setData(data);
//        response.setMessage("操作成功！");
//        response.setTimestamp(Instant.now());
//        response.setTraceId(traceId);
//        return response;
//    }
    public static <T> ApiResponse<T> error(Integer code, String traceId, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setTimestamp(Instant.now());
        response.setTraceId(traceId);
        return response;
    }

}
