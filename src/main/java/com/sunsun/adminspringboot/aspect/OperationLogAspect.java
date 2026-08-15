package com.sunsun.adminspringboot.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunsun.adminspringboot.annotation.OperationLog;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 操作日志切面：拦截 @OperationLog 注解方法，记录操作人、请求参数、IP、耗时、结果
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OperationLogAspect(OperationLogService operationLogService, UserMapper userMapper) {
        this.operationLogService = operationLogService;
        this.userMapper = userMapper;
    }

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();
        boolean success = true;
        String errorMsg = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            success = false;
            errorMsg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            throw e;
        } finally {
            try {
                record(joinPoint, operationLog, start, success, errorMsg);
            } catch (Exception ex) {
                // 日志记录失败不影响主流程
                log.warn("操作日志记录失败: {}", ex.getMessage());
            }
        }
    }

    private void record(ProceedingJoinPoint joinPoint, OperationLog operationLog,
                        long start, boolean success, String errorMsg) {
        com.sunsun.adminspringboot.entity.OperationLog entity = new com.sunsun.adminspringboot.entity.OperationLog();
        // 操作人（登录接口等未登录场景可能取不到）
        try {
            if (StpUtil.isLogin()) {
                Object loginId = StpUtil.getLoginId();
                int userId = Integer.parseInt(loginId.toString());
                entity.setUserId(userId);
                User user = userMapper.selectById(userId);
                entity.setUserName(user != null ? user.getName() : String.valueOf(userId));
            }
        } catch (Exception ignored) {
            // 忽略获取操作人失败
        }

        entity.setModule(operationLog.module());
        entity.setOperation(operationLog.operation());
        entity.setOperationType(operationLog.type().name());

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            entity.setRequestMethod(request.getMethod());
            entity.setRequestUrl(request.getRequestURI());
            entity.setRequestParams(buildParams(joinPoint));
            entity.setIp(getIp(request));
        }

        entity.setDurationMs(System.currentTimeMillis() - start);
        entity.setStatus(success ? 1 : 0);
        entity.setErrorMsg(errorMsg != null && errorMsg.length() > 500 ? errorMsg.substring(0, 500) : errorMsg);
        operationLogService.save(entity);
    }

    /** 序列化请求参数（排除文件、过长截断） */
    private String buildParams(ProceedingJoinPoint joinPoint) {
        try {
            String json = Arrays.stream(joinPoint.getArgs())
                    .filter(arg -> !(arg instanceof MultipartFile) && arg != null)
                    .map(arg -> {
                        try {
                            return objectMapper.writeValueAsString(arg);
                        } catch (Exception e) {
                            return String.valueOf(arg);
                        }
                    })
                    .collect(Collectors.joining(", "));
            return json.length() > 1000 ? json.substring(0, 1000) : json;
        } catch (Exception e) {
            return "";
        }
    }

    /** 获取客户端 IP（兼容反向代理） */
    public static String getIp(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For 可能包含多个 IP，取第一个
                int idx = ip.indexOf(',');
                return idx > 0 ? ip.substring(0, idx).trim() : ip.trim();
            }
        }
        return request.getRemoteAddr();
    }
}
