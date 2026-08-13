package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.req.UserRoleRequest;
import com.sunsun.adminspringboot.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("userRole")
@Tag(name = "用户角色关联模块", description = "用户角色查询、修改相关接口")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @Operation(summary = "查询指定用户角色ID", description = "根据用户ID查询其拥有的角色ID列表（来源于 用户-角色 关联表），用于分配角色时回显")
    @GetMapping("getRoleIds/{userId}")
    public ApiResponse<List<Integer>> getRoleIds(
            @Parameter(description = "用户ID", required = true, example = "1") @PathVariable Integer userId) {
        return ApiResponse.success(userRoleService.getRoleIds(userId));
    }

    @Operation(summary = "修改用户角色", description = "根据用户ID先删除原有角色关联，再批量设置新的角色列表；roleList 传角色ID列表，传空数组表示清空该用户所有角色")
    @PostMapping("updateRole")
    public ApiResponse<?> updateRole(@RequestBody @Valid UserRoleRequest userRoleRequest) {
        userRoleService.updateRole(userRoleRequest.getUserId(), userRoleRequest.getRoleList());
        return ApiResponse.success("修改成功");
    }
}
