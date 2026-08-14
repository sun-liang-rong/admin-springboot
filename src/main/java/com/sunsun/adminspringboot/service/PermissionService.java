package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.PermissionRequest;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<PermissionListResult> getPermissionList();
    Long newPermission(PermissionRequest permission);

    Long updatePermission(PermissionRequest permission);

    Long deletePermission(Long id);
}
