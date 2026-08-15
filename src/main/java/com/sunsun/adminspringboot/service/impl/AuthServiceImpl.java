package com.sunsun.adminspringboot.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.ChangePasswordRequest;
import com.sunsun.adminspringboot.dto.request.LoginRequest;
import com.sunsun.adminspringboot.dto.request.RegisterRequest;
import com.sunsun.adminspringboot.dto.request.UpdateProfileRequest;
import com.sunsun.adminspringboot.dto.response.LoginResult;
import com.sunsun.adminspringboot.dto.response.ProfileResult;
import com.sunsun.adminspringboot.entity.LoginLog;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.mapper.UserRoleMapper;
import com.sunsun.adminspringboot.service.AuthService;
import com.sunsun.adminspringboot.service.CaptchaService;
import com.sunsun.adminspringboot.service.LoginLogService;
import com.sunsun.adminspringboot.util.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private LoginLogService loginLogService;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LoginResult login(LoginRequest loginRequest) {
        // 1. 校验验证码（app.captcha.enabled=false 时跳过）
        if (!captchaService.verify(loginRequest.getCaptchaId(), loginRequest.getCaptchaCode())) {
            recordLoginLog(loginRequest.getName(), 0, "验证码错误");
            throw new BusinessException("验证码错误");
        }

        // 2. 校验账号密码
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("name", "email", "id")
                .eq("name", loginRequest.getName())
                .eq("password", loginRequest.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            recordLoginLog(loginRequest.getName(), 0, "用户名或密码错误");
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 登录并记录会话信息（在线用户/登录日志使用）
        StpUtil.login(user.getId());
        StpUtil.getTokenSession().set("loginTime", LocalDateTime.now().format(TIME_FORMAT));
        StpUtil.getTokenSession().set("loginIp", getClientIp());
        recordLoginLog(user.getName(), 1, "登录成功");

        // 4. 组装登录结果
        List<String> role = userRoleMapper.selectUserRoleKeys(user.getId().longValue());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginResult loginResult = new LoginResult();
        loginResult.setEmail(user.getEmail());
        loginResult.setUserId(user.getId());
        loginResult.setName(user.getName());
        loginResult.setRole(role);
        loginResult.setToken(tokenInfo.tokenValue);
        return loginResult;
    }

    /** 记录登录日志（成功/失败） */
    private void recordLoginLog(String userName, int status, String message) {
        try {
            HttpServletRequest request = getRequest();
            String ua = request == null ? null : request.getHeader("User-Agent");
            LoginLog log = new LoginLog();
            log.setUserName(userName);
            log.setIp(getClientIp());
            log.setBrowser(UserAgentUtil.parseBrowser(ua));
            log.setOs(UserAgentUtil.parseOs(ua));
            log.setStatus(status);
            log.setMessage(message);
            log.setLoginTime(LocalDateTime.now().format(TIME_FORMAT));
            loginLogService.save(log);
        } catch (Exception ignored) {
            // 日志记录失败不影响登录主流程
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }

    private String getClientIp() {
        HttpServletRequest request = getRequest();
        if (request == null) return "unknown";
        return com.sunsun.adminspringboot.aspect.OperationLogAspect.getIp(request);
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

    /** 当前登录用户 ID */
    private Integer currentUserId() {
        return Integer.valueOf(StpUtil.getLoginId().toString());
    }

    @Override
    public ProfileResult getProfile() {
        Integer userId = currentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        ProfileResult profile = new ProfileResult();
        profile.setUserId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setRoles(userRoleMapper.selectUserRoleKeys(userId.longValue()));
        return profile;
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Integer userId = currentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
            throw new BusinessException("原密码不正确");
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(changePasswordRequest.getNewPassword());
        userMapper.updateById(update);
    }

    @Override
    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        Integer userId = currentUserId();
        // 邮箱唯一性校验（排除自己）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getEmail, updateProfileRequest.getEmail())
                .ne(User::getId, userId);
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该邮箱已被其他账号使用");
        }
        User update = new User();
        update.setId(userId);
        update.setEmail(updateProfileRequest.getEmail());
        userMapper.updateById(update);
    }
}
