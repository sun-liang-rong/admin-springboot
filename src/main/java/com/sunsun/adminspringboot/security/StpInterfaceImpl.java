package com.sunsun.adminspringboot.security;

import cn.dev33.satoken.stp.StpInterface;
import com.sunsun.adminspringboot.mapper.UserMapper;
import com.sunsun.adminspringboot.mapper.UserRoleMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = userMapper.selectUserPermissionKeys(loginIdToLong(loginId));
        System.out.println("permissionList: " + permissionList);
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = userRoleMapper.selectUserRoleKeys(loginIdToLong(loginId));
        System.out.println("roleList: " + roleList);
        if (roleList.contains("super-admin")) {
            return List.of("*");
        }
        return roleList;
    }

    /**
     * Sa-Token 传入的 loginId 类型与登录时 StpUtil.login(id) 的入参一致（此处为 Integer），
     * 统一转成 Long 再用于 SQL 查询，避免 ClassCastException。
     */
    private Long loginIdToLong(Object loginId) {
        return Long.parseLong(String.valueOf(loginId));
    }
}
