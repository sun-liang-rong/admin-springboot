package com.sunsun.adminspringboot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.sunsun.adminspringboot.dto.response.OnlineUserResult;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.service.OnlineUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<OnlineUserResult> list(String keyword) {
        List<OnlineUserResult> result = new ArrayList<>();
        // 搜索全部在线 token（分页遍历，一次最多取 1000）
        // 注意：配置了 token-name 时，searchTokenValue 返回的是完整存储键（形如 authorization:login:token:xxx），需剥离前缀
        String keyPrefix = StpUtil.getTokenName() + ":login:token:";
        List<String> keys = StpUtil.searchTokenValue("", 0, 1000, false);
        for (String key : keys) {
            String token = key.startsWith(keyPrefix) ? key.substring(keyPrefix.length()) : key;
            Object loginId;
            try {
                loginId = StpUtil.getLoginIdByToken(token);
            } catch (Exception e) {
                // token 已失效（列表与查询之间的时间窗），跳过
                continue;
            }
            if (loginId == null) {
                // 存储中无对应会话，跳过
                continue;
            }
            Integer userId = Integer.valueOf(loginId.toString());
            User user = userMapper.selectById(userId);
            String userName = user != null ? user.getName() : String.valueOf(userId);
            // 关键字过滤（用户名 / 用户ID）
            if (keyword != null && !keyword.isBlank()) {
                if (!userName.contains(keyword) && !String.valueOf(userId).contains(keyword)) {
                    continue;
                }
            }
            OnlineUserResult item = new OnlineUserResult();
            item.setUserId(userId);
            item.setUserName(userName);
            item.setToken(token);
            item.setLoginTime((String) StpUtil.getTokenSessionByToken(token).get("loginTime"));
            item.setLoginIp((String) StpUtil.getTokenSessionByToken(token).get("loginIp"));
            item.setTimeout(StpUtil.getTokenTimeout(token));
            result.add(item);
        }
        return result;
    }

    @Override
    public void kick(String tokenValue) {
        // 注销指定 token 的会话
        StpUtil.logoutByTokenValue(tokenValue);
    }
}
