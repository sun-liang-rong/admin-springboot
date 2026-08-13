package com.sunsun.adminspringboot.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.req.RolePermissionRequest;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.entity.Permission;
import com.sunsun.adminspringboot.entity.Role;
import com.sunsun.adminspringboot.entity.RolePermission;
import com.sunsun.adminspringboot.mapper.PermissionMapper;
import com.sunsun.adminspringboot.mapper.RoleMapper;
import com.sunsun.adminspringboot.mapper.RolePermissionMapper;
import com.sunsun.adminspringboot.service.RolePermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RoleMapper roleMapper;
    @Override
    public List<Integer> getRolePermission(Integer roleId) {
        // 判断是不是超级管理员
        Role role = roleMapper.selectById(roleId);
        if (role.getName().equals("super-admin")) {
            // 超级管理员有所有权限
            LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<Permission>();
            wrapper.eq(Permission::getStatus, 1);
            return permissionMapper.selectList(wrapper).stream().map(Permission::getId).toList();
        }
        List<Permission> list = rolePermissionMapper.getRolePermission(roleId);

        // 转换 Permission 实体为响应 DTO
        return list.stream().map(Permission::getId).collect(Collectors.toList());
    }

    @Override
    public void updateRolePermission(RolePermissionRequest rolePermissionRequest) {
        try {
            // 先删除当前角色关联的权限
            LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<RolePermission>();
            wrapper.eq(RolePermission::getRoleId, rolePermissionRequest.getRoleId());
            rolePermissionMapper.delete(wrapper);
            // 批量添加新的权限关联（空列表/未传则仅清空）
            if (rolePermissionRequest.getPermissionList() != null && !rolePermissionRequest.getPermissionList().isEmpty()) {
                rolePermissionMapper.insertBatchPermission(rolePermissionRequest.getRoleId(), rolePermissionRequest.getPermissionList());
            }
        } catch (Exception e) {
            throw new BusinessException(500, "更新角色权限失败");
        }
    }
}
