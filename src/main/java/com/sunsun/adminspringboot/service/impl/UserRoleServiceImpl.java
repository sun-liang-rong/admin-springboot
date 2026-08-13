package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.entity.UserRole;
import com.sunsun.adminspringboot.mapper.UserRoleMapper;
import com.sunsun.adminspringboot.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Integer> getRoleIds(Integer userId) {
        return userRoleMapper.selectUserRoleIds(userId.longValue());
    }

    @Override
    public void updateRole(Integer userId, List<Integer> roleList) {
        try {
            // 删除用户原有角色关联
            LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<UserRole>();
            wrapper.eq(UserRole::getUserId, userId);
            userRoleMapper.delete(wrapper);
            // 批量插入新的角色关联
            if (roleList != null && !roleList.isEmpty()) {
                List<UserRole> userRoles = roleList.stream().map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(roleId);
                    userRole.setUserId(userId);
                    return userRole;
                }).toList();
                userRoleMapper.insertBatch(userRoles);
            }
        } catch (Exception e) {
            // 处理异常
            throw new BusinessException(500, "更新用户角色失败");
        }
    }
}
