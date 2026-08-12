package com.sunsun.adminspringboot.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.req.LoginRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public LoginResult login(LoginRequest loginRequest) {
        System.out.println(loginRequest);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("name", "email", "id")
                .eq("name", loginRequest.getName())
                .eq("password", loginRequest.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        StpUtil.login(user.getId());
        List<String> role = userMapper.selectUserRoleKeys(user.getId().longValue());
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginResult loginResult = new LoginResult();
        loginResult.setEmail(user.getEmail());
        loginResult.setUserId(user.getId());
        loginResult.setName(user.getName());
        loginResult.setRole(role);
        loginResult.setToken(tokenInfo.tokenValue);
        return loginResult;
    }
}
