package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.query.RolePageQuery;
import com.sunsun.adminspringboot.dto.request.req.RoleRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.Role;

import java.util.List;

public interface RoleService {
    PageResult<Role> getRoleList(RolePageQuery rolePageQuery); // 获取角色列表
    Role newRole(RoleRequest roleRequest);   // 新建角色
    Role updateRole(RoleRequest roleRequest); // 修改角色
    void deleteRole(Long id); // 删除角色
}
