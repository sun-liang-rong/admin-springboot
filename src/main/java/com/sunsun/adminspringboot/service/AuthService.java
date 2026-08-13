package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.req.LoginRequest;
import com.sunsun.adminspringboot.dto.request.req.RegisterRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;

public interface AuthService {
    LoginResult login(LoginRequest loginRequest);
    Long register(RegisterRequest registerRequest);
}
