package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.PermissionRequest;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.service.PermissionService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permission")
@Tag(name = "权限管理模块", description = "权限新增、查询、修改、删除相关接口")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    @Operation(summary = "获取权限树形列表", description = "返回以顶级节点（parentId=0）为根节点的树形权限列表")
    @SaCheckPermission("system:permission:list")
    @GetMapping("/getPermissionList")
    public ApiResponse<List<PermissionListResult>> getPermissionList() {
        return ApiResponse.success(permissionService.getPermissionList());
    }

    @Operation(summary = "新增权限", description = "创建一条新权限（新增时无需传递 id）")
    @SaCheckPermission("system:permission:add")
    @PostMapping("/newPermission")
    public ApiResponse<Long> newPermission(@RequestBody @Valid PermissionRequest permission) {
        return ApiResponse.success(permissionService.newPermission(permission));
    }

    @Operation(summary = "修改权限", description = "修改权限信息（修改时必须传递 id）")
    @SaCheckPermission("system:permission:edit")
    @PostMapping("/updatePermission")
    public ApiResponse<Long> updatePermission(@RequestBody @Valid PermissionRequest permission) {
        return ApiResponse.success(permissionService.updatePermission(permission));
    }

    @Operation(summary = "删除权限", description = "根据权限 id 删除指定权限")
    @SaCheckPermission("system:permission:delete")
    @DeleteMapping("/deletePermission/{id}")
    public ApiResponse<Long> deletePermission(@Parameter(description = "权限ID", required = true, example = "1") @PathVariable Long id) {
        return ApiResponse.success(permissionService.deletePermission(id));
    }
}
