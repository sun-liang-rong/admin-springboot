package com.sunsun.adminspringboot.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "统一响应结果")
public class ApiResponse<T> {

    @Schema(description = "响应状态码，200 表示成功", example = "200")
    private int code;

    @Schema(description = "响应消息", example = "操作成功！")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "链路追踪ID", example = "a1b2c3d4")
    private String traceId;

    @Schema(description = "响应时间戳")
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
