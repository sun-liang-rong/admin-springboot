package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

@Data
public class Permission {
    // 权限ID
    private Integer id;
    // 父权限ID（0=顶级，仅目录可为0；菜单的父=目录，按钮的父=菜单）
    private Integer parentId;
    // 权限类型 1目录 2菜单 3按钮
    private Integer perType;
    // 显示名称（目录名称/菜单名称/按钮权限名称）
    private String name;
    // 权限字符（仅按钮必填，例如：user:add）
    private String perKey;
    // 路由地址（目录/菜单用）
    private String path;
    // 组件路径（菜单必填，目录可填Layout）
    private String component;
    // 图标（目录/菜单用）
    private String icon;
    // 是否缓存页面keep-alive（菜单用）0否 1是
    private Integer isCache;
    // 是否显示（目录/菜单用）0不显示 1显示
    private Integer visible;
    // 排序编号
    private Integer sortNum;
    // 状态 0禁用 1启用
    private Integer status;
    // 创建时间
    private String createTime;
    // 更新时间
    private String updateTime;
    @TableField(exist = false)
    // 存放子菜单，用来构成树形
    private List<Permission> children;
}
