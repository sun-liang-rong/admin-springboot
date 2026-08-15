package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.ChangePasswordRequest;
import com.sunsun.adminspringboot.dto.request.LoginRequest;
import com.sunsun.adminspringboot.dto.request.RegisterRequest;
import com.sunsun.adminspringboot.dto.request.UpdateProfileRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.dto.response.ProfileResult;

public interface AuthService {
    LoginResult login(LoginRequest loginRequest);
    Long register(RegisterRequest registerRequest);

    /** 获取当前登录用户信息（个人中心） */
    ProfileResult getProfile();

    /** 修改密码（校验原密码后更新） */
    void changePassword(ChangePasswordRequest changePasswordRequest);

    /** 更新个人资料（邮箱） */
    void updateProfile(UpdateProfileRequest updateProfileRequest);
}
