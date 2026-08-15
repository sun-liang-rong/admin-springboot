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

    /** 全部启用状态的目录/菜单路由地址（登录即可调用，供前端 403 判断） */
    List<String> getAllMenuPaths();
}
