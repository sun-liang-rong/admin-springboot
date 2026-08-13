package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("permission_role")
public class RolePermission {
    private Integer id;
    @TableField("role_id")
    private Integer roleId;
    @TableField("permission_id")
    private Integer permissionId;
}
