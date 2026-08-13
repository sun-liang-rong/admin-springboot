package com.sunsun.adminspringboot.service;

import java.util.List;

public interface UserRoleService {

    // 根据用户ID查询已分配的角色ID列表（用于分配角色时回显）
    List<Integer> getRoleIds(Integer userId);

    // 修改用户的角色列表
    void updateRole(Integer userId, List<Integer> roleList);
}
