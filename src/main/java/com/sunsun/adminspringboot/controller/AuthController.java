package com.sunsun.adminspringboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sunsun.adminspringboot.annotation.OperationLog;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.common.enums.OperationType;
import com.sunsun.adminspringboot.dto.request.ChangePasswordRequest;
import com.sunsun.adminspringboot.dto.request.LoginRequest;
import com.sunsun.adminspringboot.dto.request.RegisterRequest;
import com.sunsun.adminspringboot.dto.request.UpdateProfileRequest;
import com.sunsun.adminspringboot.dto.response.CaptchaResult;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.dto.response.ProfileResult;
import com.sunsun.adminspringboot.service.AuthService;
import com.sunsun.adminspringboot.service.CaptchaService;
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
    @Autowired
    private CaptchaService captchaService;

    @Operation(summary = "获取图形验证码", description = "返回验证码ID与base64图片，登录时回传 captchaId 与 captchaCode 进行校验")
    @GetMapping("/captcha")
    public ApiResponse<CaptchaResult> captcha() {
        return ApiResponse.success(captchaService.generate());
    }

    @Operation(summary = "用户登录", description = "根据用户名和密码进行登录，成功后返回 token、用户信息及用户角色列表")
    @OperationLog(module = "认证管理", operation = "用户登录", type = OperationType.LOGIN)
    @PostMapping("/login")
    public ApiResponse<LoginResult> login(@RequestBody @Valid LoginRequest loginRequest){
        LoginResult data = authService.login(loginRequest);
        return ApiResponse.success(data);
    }

    @Operation(summary = "退出登录", description = "使当前会话失效，退出登录")
    @OperationLog(module = "认证管理", operation = "退出登录", type = OperationType.LOGOUT)
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

    @Operation(summary = "获取当前用户信息", description = "返回当前登录用户的 ID、用户名、邮箱与角色列表（个人中心使用）")
    @GetMapping("/profile")
    public ApiResponse<ProfileResult> profile(){
        return ApiResponse.success(authService.getProfile());
    }

    @Operation(summary = "修改密码", description = "校验原密码后修改为新的密码，修改成功后当前会话保持登录")
    @PostMapping("/changePassword")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest){
        authService.changePassword(changePasswordRequest);
        return ApiResponse.success(null);
    }

    @Operation(summary = "更新个人资料", description = "更新当前登录用户的邮箱（用户名作为登录标识不可修改）")
    @PostMapping("/updateProfile")
    public ApiResponse<Void> updateProfile(@RequestBody @Valid UpdateProfileRequest updateProfileRequest){
        authService.updateProfile(updateProfileRequest);
        return ApiResponse.success(null);
    }
}