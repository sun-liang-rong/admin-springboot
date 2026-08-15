package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.annotation.OperationLog;
import com.sunsun.adminspringboot.common.enums.OperationType;
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
    @OperationLog(module = "权限管理", operation = "新增权限", type = OperationType.INSERT)
    public ApiResponse<Long> newPermission(@RequestBody @Valid PermissionRequest permission) {
        return ApiResponse.success(permissionService.newPermission(permission));
    }

    @Operation(summary = "修改权限", description = "修改权限信息（修改时必须传递 id）")
    @SaCheckPermission("system:permission:edit")
    @PostMapping("/updatePermission")
    @OperationLog(module = "权限管理", operation = "修改权限", type = OperationType.UPDATE)
    public ApiResponse<Long> updatePermission(@RequestBody @Valid PermissionRequest permission) {
        return ApiResponse.success(permissionService.updatePermission(permission));
    }

    @Operation(summary = "删除权限", description = "根据权限 id 删除指定权限")
    @SaCheckPermission("system:permission:delete")
    @DeleteMapping("/deletePermission/{id}")
    @OperationLog(module = "权限管理", operation = "删除权限", type = OperationType.DELETE)
    public ApiResponse<Long> deletePermission(@Parameter(description = "权限ID", required = true, example = "1") @PathVariable Long id) {
        return ApiResponse.success(permissionService.deletePermission(id));
    }

    @Operation(summary = "获取全部菜单路由地址", description = "返回所有启用状态的目录与菜单的路由地址列表（登录即可调用，无需额外权限），供前端区分 403 无权限与 404 页面不存在")
    @GetMapping("/getAllMenuPaths")
    public ApiResponse<List<String>> getAllMenuPaths() {
        return ApiResponse.success(permissionService.getAllMenuPaths());
    }
}
