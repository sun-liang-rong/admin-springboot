package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.req.RolePermissionRequest;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;

import java.util.List;

public interface RolePermissionService {
    List<Integer> getRolePermission(Integer roleId);

    void updateRolePermission(RolePermissionRequest rolePermissionRequest);
}
