package com.sunsun.adminspringboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.req.LoginRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "认证管理模块", description = "用户登录、退出登录相关接口")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录，成功后返回 token、用户信息及用户角色列表")
    @PostMapping("/login")
    public ApiResponse<LoginResult> login(@RequestBody @Valid LoginRequest loginRequest){
        LoginResult data = authService.login(loginRequest);
        return ApiResponse.success(data);
    }

    @Operation(summary = "退出登录", description = "使当前会话失效，退出登录")
    @PostMapping("/loginOut")
    public ApiResponse<LoginResult> loginOut(){
        StpUtil.logout();
        return ApiResponse.success(null);
    }
}