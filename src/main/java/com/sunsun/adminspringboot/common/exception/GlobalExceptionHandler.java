package com.sunsun.adminspringboot.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.common.TraceContext;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>统一捕获 Controller/Service 层抛出的异常，封装为统一格式的 ApiResponse 返回给前端</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 生成简易 traceId，用于日志与响应关联
     */
    private String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return ApiResponse.error(e.getCode(), genTraceId(), e.getMessage());
    }

    /**
     * 参数校验异常（@RequestBody @Valid 及表单/query 参数绑定 @Valid 对象）
     * <p>MethodArgumentNotValidException 继承自 BindException，此处一并处理</p>
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return ApiResponse.error(400, genTraceId(), message);
    }

    /**
     * 参数校验异常（@RequestParam / @PathVariable 上的约束注解）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败: {}", message);
        return ApiResponse.error(400, genTraceId(), message);
    }

    /**
     * 请求参数类型转换失败，例如 age 传入 abc
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "参数 " + e.getName() + " 类型不正确，期望类型: " + e.getRequiredType().getSimpleName();
        log.warn("参数类型错误: {}", message);
        return ApiResponse.error(400, genTraceId(), message);
    }

    /**
     * 请求体 JSON 解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败", e);
        return ApiResponse.error(400, genTraceId(), "请求体格式不正确");
    }

    /**
     * 缺少必填请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = "缺少必填参数: " + e.getParameterName();
        log.warn("缺少参数: {}", e.getParameterName());
        return ApiResponse.error(400, genTraceId(), message);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = "请求方式不支持，当前请求方法: " + e.getMethod() + "，支持的方法: " + String.join(", ", e.getSupportedMethods());
        log.warn("请求方式不支持: {}", message);
        return ApiResponse.error(405, genTraceId(), message);
    }

    /**
     * 资源不存在（前端请求了不存在的接口路径）
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("资源不存在: {}", e.getResourcePath());
        return ApiResponse.error(404, genTraceId(), "请求的资源不存在");
    }
    // 未登录异常
    @ExceptionHandler(NotLoginException.class)
    public ApiResponse<?> handleNotLoginException(NotLoginException e) {
        String msg = switch (e.getType()) {
            case NotLoginException.TOKEN_TIMEOUT -> "token已过期，请重新登录";
            case NotLoginException.INVALID_TOKEN -> "无效token";
            case NotLoginException.NOT_TOKEN -> "未携带Authorization凭证";
            default -> "登录认证失败";
        };
        return ApiResponse.error(401, TraceContext.getTraceId(), msg);
    }

    // 缺少权限
    @ExceptionHandler(NotPermissionException.class)
    public ApiResponse<?> handleNotPermissionException(NotPermissionException e) {
        return ApiResponse.error(403, TraceContext.getTraceId(), "没有操作权限：" + e.getPermission());
    }

    // 缺少角色
    @ExceptionHandler(NotRoleException.class)
    public ApiResponse<?> handleNotRoleException(NotRoleException e) {
        return ApiResponse.error(403, TraceContext.getTraceId(), "用户角色不足：" + e.getRole());
    }

    /**
     * 兜底异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ApiResponse.error(500, genTraceId(), "系统内部错误，请稍后重试");
    }
}