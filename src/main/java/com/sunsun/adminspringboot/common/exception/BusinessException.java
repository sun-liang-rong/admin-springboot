package com.sunsun.adminspringboot.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * <p>业务代码中主动抛出，由全局异常处理器统一捕获并返回给前端</p>
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 业务错误码，默认 400
     */
    private final int code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}