package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunsun.adminspringboot.dto.request.query.RolePageQuery;
import com.sunsun.adminspringboot.dto.request.req.RoleRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.Role;
import com.sunsun.adminspringboot.entity.User;
import com.sunsun.adminspringboot.mapper.RoleMapper;
import com.sunsun.adminspringboot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public PageResult<Role> getRoleList(RolePageQuery rolePageQuery) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (rolePageQuery.getRoleName() != null) {
            lambdaQueryWrapper.like(Role::getName, rolePageQuery.getRoleName());
        }
        Page<Role> page = new Page<>(rolePageQuery.getPageNum(), rolePageQuery.getPageSize());
        IPage<Role> rolePage = roleMapper.selectPage(page, lambdaQueryWrapper);
        return PageResult.of(rolePage);
    }
    @Override
    public Role newRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        if(roleMapper.insert(role) > 0) {
            return role;
        }
        throw new RuntimeException("新增失败");
    }
    @Override
    public Role updateRole(RoleRequest roleRequest) {
        if (roleRequest.getId() == null) {
            throw new RuntimeException("角色ID不能为空");
        }
        Role role = new Role();
        role.setId(roleRequest.getId());
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        if(roleMapper.updateById(role) > 0) {
            return role;
        }
        throw new RuntimeException("更新失败");
    }
    @Override
    public void deleteRole(Long id) {
        roleMapper.deleteById(id);
    }
}
