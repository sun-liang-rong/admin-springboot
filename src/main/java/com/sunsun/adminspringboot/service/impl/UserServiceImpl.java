package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunsun.adminspringboot.dto.request.query.UserPageQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<User> list(UserPageQuery userPageQuery) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        // 根据用户名模糊查询
        if (StringUtils.hasText(userPageQuery.getName())) {
            wrapper.like(User::getName, userPageQuery.getName());
        }
        // 根据年龄精确查询
        if (userPageQuery.getAge() != null) {
            wrapper.eq(User::getAge, userPageQuery.getAge());
        }
        // 根据邮箱模糊查询
        if (StringUtils.hasText(userPageQuery.getEmail())) {
            wrapper.like(User::getEmail, userPageQuery.getEmail());
        }
        // 分页查询并转换为统一分页响应
        Page<User> page = new Page<>(userPageQuery.getPageNum(), userPageQuery.getPageSize());
        IPage<User> result = userMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }
}
