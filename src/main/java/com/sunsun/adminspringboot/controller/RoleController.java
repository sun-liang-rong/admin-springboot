package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.RolePageQuery;
import com.sunsun.adminspringboot.dto.request.RoleRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.Role;
import com.sunsun.adminspringboot.service.RoleService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Tag(name = "角色管理模块", description = "角色新增、查询、修改、删除相关接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Operation(summary = "分页查询角色列表", description = "支持按角色名条件筛选，返回分页角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/getRoleList")
    public ApiResponse<PageResult<Role>> getRoleList(@ParameterObject @Valid RolePageQuery rolePageQuery) {
        return ApiResponse.success(roleService.getRoleList(rolePageQuery));
    }

    @Operation(summary = "新建角色", description = "创建一个新角色（新增时无需传递 id）")
    @SaCheckPermission("system:role:add")
    @PostMapping("/newRole")
    public ApiResponse<Role> newRole(@RequestBody @Valid RoleRequest roleRequest) {
        Role role = roleService.newRole(roleRequest);
        return ApiResponse.success(role);
    }

    @Operation(summary = "修改角色", description = "修改角色信息（修改时必须传递 id）")
    @SaCheckPermission("system:role:edit")
    @PostMapping("/updateRole")
    public ApiResponse<Role> updateRole(@RequestBody @Valid RoleRequest roleRequest) {
        Role role = roleService.updateRole(roleRequest);
        return ApiResponse.success(role);
    }

    @Operation(summary = "删除角色", description = "根据角色 id 删除指定角色")
    @SaCheckPermission("system:role:delete")
    @DeleteMapping("/deleteRole/{id}")
    public ApiResponse<Void> deleteRole(@Parameter(description = "角色ID", required = true, example = "1") @PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success(null);
    }
}