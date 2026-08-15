package com.sunsun.adminspringboot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunsun.adminspringboot.entity.Permission;
import com.sunsun.adminspringboot.entity.RolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    // 注意：必须用 p.* 而不能 SELECT *，否则结果集有两个 id 列，MyBatis 会取到关联表 permission_role 的自增 id 而非真正的权限 id
    @Select("SELECT p.* FROM permission_role pr JOIN permission p ON pr.permission_id = p.id WHERE pr.role_id = #{roleId}")
    List<Permission> getRolePermission(Integer roleId);
    @Insert("<script>" +
            "INSERT INTO permission_role (role_id, permission_id) VALUES " +
            "<foreach collection='permissionList' item='permissionId' separator=','>" +
            "(#{roleId}, #{permissionId})" +
            "</foreach>" +
            "</script>")
    void insertBatchPermission(Integer roleId, List<Integer> permissionList);
}
