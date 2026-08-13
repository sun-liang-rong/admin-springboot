package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.req.RolePermissionRequest;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.service.RolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rolePermission")
@Tag(name = "角色权限关联模块", description = "角色权限查询、修改相关接口")
public class RolePermissionController {
    @Resource
    private RolePermissionService rolePermissionService;

    // 获取角色的权限
    @Operation(summary = "查询角色权限", description = "根据角色ID查询该角色拥有的权限列表（返回权限ID、名称、权限字符、路由等完整信息）")
    @GetMapping("/getRolePermission/{roleId}")
    public ApiResponse<List<Integer>> getRolePermission(
            @Parameter(description = "角色ID", required = true, example = "1") @PathVariable Integer roleId) {
        return ApiResponse.success(rolePermissionService.getRolePermission(roleId));
    }

    // 修改角色的权限
    @Operation(summary = "修改角色权限", description = "根据角色ID先删除原有权限关联，再批量设置新的权限列表；permissionList 传权限ID列表，传空数组表示清空该角色所有权限")
    @PostMapping("/updateRolePermission")
    public ApiResponse<?> updateRolePermission(@RequestBody @Valid RolePermissionRequest rolePermissionRequest) {
        rolePermissionService.updateRolePermission(rolePermissionRequest);
        return ApiResponse.success("修改成功");
    }
}
