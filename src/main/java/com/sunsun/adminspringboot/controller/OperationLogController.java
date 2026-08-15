package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.OperationLogQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.OperationLog;
import com.sunsun.adminspringboot.service.OperationLogService;
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
@RequestMapping("log/operation")
@Tag(name = "操作日志模块", description = "操作日志查询")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @Operation(summary = "分页查询操作日志", description = "支持按操作人、模块、操作类型、状态、时间范围筛选")
    @SaCheckPermission("system:operationLog:list")
    @GetMapping("/list")
    public ApiResponse<PageResult<OperationLog>> list(@ParameterObject @Valid OperationLogQuery query) {
        return ApiResponse.success(operationLogService.list(query));
    }
}
