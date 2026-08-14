package com.sunsun.adminspringboot.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.LoginRequest;
import com.sunsun.adminspringboot.dto.request.RegisterRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.mapper.UserRoleMapper;
import com.sunsun.adminspringboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
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
        List<String> role = userRoleMapper.selectUserRoleKeys(user.getId().longValue());
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
    public Long register(RegisterRequest registerRequest) {
        // 判断是不是已经存在
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getName, User::getEmail)
                .eq(User::getName, registerRequest.getName())
                .or()
                .eq(User::getEmail, registerRequest.getEmail());
        User user = userMapper.selectOne(lambdaQueryWrapper);
        if (user != null) {
            throw new BusinessException("用户已存在");
        }
        User newUser = new User();
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(registerRequest.getPassword());
        userMapper.insert(newUser);
        return newUser.getId().longValue();
    }
}
