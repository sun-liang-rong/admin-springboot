package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.LoginRequest;
import com.sunsun.adminspringboot.dto.request.RegisterRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;

public interface AuthService {
    LoginResult login(LoginRequest loginRequest);
    Long register(RegisterRequest registerRequest);
}
