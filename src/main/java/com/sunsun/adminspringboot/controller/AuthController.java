package com.sunsun.adminspringboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.req.LoginRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ApiResponse<LoginResult> login(@RequestBody @Valid LoginRequest loginRequest){
        LoginResult data = authService.login(loginRequest);
        return ApiResponse.success(data);
    }
    @PostMapping("/loginOut")
    public ApiResponse<LoginResult> loginOut(){
        StpUtil.logout();
        return ApiResponse.success(null);
    }
}
