package com.sunsun.adminspringboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.req.LoginRequest;
import com.sunsun.adminspringboot.dto.request.req.RegisterRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "认证管理模块", description = "用户注册、登录、退出登录相关接口")
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

    @Operation(summary = "用户注册", description = "根据用户名、邮箱和密码进行注册，用户名或邮箱已存在时返回业务码 400（用户已存在），成功后返回新用户ID")
    @PostMapping("/register")
    public ApiResponse<Long> register(@RequestBody @Valid RegisterRequest registerRequest){
        Long data = authService.register(registerRequest);
        return ApiResponse.success(data);
    }
}