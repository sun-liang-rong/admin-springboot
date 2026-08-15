package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.LoginLogQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.LoginLog;
import com.sunsun.adminspringboot.service.LoginLogService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log/login")
@Tag(name = "登录日志模块", description = "登录日志查询")
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;

    @Operation(summary = "分页查询登录日志", description = "支持按用户名、状态、时间范围筛选")
    @SaCheckPermission("system:loginLog:list")
    @GetMapping("/list")
    public ApiResponse<PageResult<LoginLog>> list(@ParameterObject @Valid LoginLogQuery query) {
        return ApiResponse.success(loginLogService.list(query));
    }
}
