package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.req.LoginRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Resource
    private AuthService authService;
    @PostMapping("/login")
    public ApiResponse<LoginResult> login(@Valid LoginRequest loginRequest){
        LoginResult data = authService.login(loginRequest);
        return ApiResponse.success(data);
    }
}
