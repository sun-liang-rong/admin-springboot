package com.sunsun.adminspringboot.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.UserPageQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
@Tag(name = "用户管理模块", description = "用户新增、查询、修改、删除相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询用户", description = "支持按用户名、年龄、邮箱条件筛选，返回分页用户列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("list")
    public ApiResponse<PageResult<User>> list(@ParameterObject @Valid UserPageQuery userPageQuery) {
        PageResult<User> list = userService.list(userPageQuery);
        return ApiResponse.success(list);
    }
    @Operation(summary = "获取当前用户菜单", description = "根据当前登录用户返回其可见的菜单列表（目录+菜单），已组装为树形结构返回；超级管理员返回全部启用状态的菜单")
    @GetMapping("menu")
    public ApiResponse<List<PermissionListResult>> getMenu() {
        return ApiResponse.success(userService.getMenu(Integer.valueOf(StpUtil.getLoginId().toString())));
    }
    @Operation(summary = "获取当前用户权限", description = "根据当前登录用户返回其拥有的权限字符列表（如 system:user:add）；超级管理员返回全部权限字符")
    @GetMapping("permission")
    public ApiResponse<?> getPermission() {
        return ApiResponse.success(userService.getPermission(Integer.valueOf(StpUtil.getLoginId().toString())));
    }
}