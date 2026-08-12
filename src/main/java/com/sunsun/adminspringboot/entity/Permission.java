package com.sunsun.adminspringboot.entity;

import lombok.Data;

@Data
public class Permission {
    // 权限ID
    private Integer id;
    // 父权限ID
    private Integer parent_id;
    // 权限名称
    private String per_name;
    // 权限标识
    private String per_key;
    // 权限类型
    private String per_type;
    // 路径
    private String path;
    // 组件
    private String component;
    // 图标
    private String icon;
    // 排序编号
    private Integer sort_num;
    // 是否可见
    private Integer visible;
    // 状态
    private Integer status;
    // 创建时间
    private String create_time;
    // 更新时间
    private String update_time;
}
