package com.sunsun.adminspringboot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.sunsun.adminspringboot.util.PermissionTreeBuilder;
import com.sunsun.adminspringboot.dto.request.UserPageQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.entity.Permission;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.PermissionMapper;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public PageResult<User> list(UserPageQuery userPageQuery) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        // 根据用户名模糊查询
        if (StringUtils.hasText(userPageQuery.getName())) {
            wrapper.like(User::getName, userPageQuery.getName());
        }
        // 根据邮箱模糊查询
        if (StringUtils.hasText(userPageQuery.getEmail())) {
            wrapper.like(User::getEmail, userPageQuery.getEmail());
        }
        wrapper.select(
                User::getId,
                User::getName,
                User::getEmail
                // 把业务需要的字段全部列出来，不要写 User::getPassword
        );
        // 分页查询并转换为统一分页响应
        Page<User> page = new Page<>(userPageQuery.getPageNum(), userPageQuery.getPageSize());
        IPage<User> result = userMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }

    @Override
    public List<PermissionListResult> getMenu(Integer userId) {
        List<Permission> menu;
        if (StpUtil.hasRole("super-admin")) {
            // 超级管理员：查询出所有启用状态的目录+菜单
            LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<Permission>();
            wrapper.eq(Permission::getStatus, 1)
                    .in(Permission::getPerType, 1, 2)
                    .orderByAsc(Permission::getSortNum);
            menu = permissionMapper.selectList(wrapper);
        } else {
            // 普通用户：按用户-角色-权限关联查询
            menu = userMapper.selectUserMenuKeys(userId.longValue());
        }
        // 转 DTO 并组装成树形结构返回
        List<PermissionListResult> menuDto = menu.stream().map(PermissionListResult::of).collect(Collectors.toList());
        return PermissionTreeBuilder.buildTree(menuDto);
    }

    @Override
    public List<String> getPermission(Integer userId) {
        // 判断当前账号是不是超级管理员
        if (StpUtil.hasRole("super-admin")) {
           // 查询出所有权限
            LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<Permission>();
            lambdaQueryWrapper.eq(Permission::getStatus, 1).eq(Permission::getPerType, 3);
            return permissionMapper.selectList(lambdaQueryWrapper).stream()
                    .map(Permission::getPerKey)
                    .collect(Collectors.toList());
        }
        return userMapper.selectUserPermissionKeys(Long.parseLong(String.valueOf(userId)));
    }
}
