package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UserRole {
    // 用户角色ID
    private Integer id;
    // 用户ID
    @TableField("user_id")
    private Integer userId;
    // 角色ID
    @TableField("role_id")
    private Integer roleId;
}
