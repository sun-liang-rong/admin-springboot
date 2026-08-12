package com.sunsun.adminspringboot.security;

import cn.dev33.satoken.stp.StpInterface;
import com.sunsun.adminspringboot.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class StpInterfaceImpl implements StpInterface {
    @Resource
    private UserMapper userMapper;
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permissionList = userMapper.selectUserPermissionKeys((Long) loginId);
        System.out.println("permissionList: " + permissionList);
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = userMapper.selectUserRoleKeys((Long) loginId);
        System.out.println("roleList: " + roleList);
        if (roleList.contains("super-admin")) {
            return List.of("*");
        }
        return roleList;
    }
}
