-- 创建数据库 admin-spring-boot 如果数据库存在则忽略
CREATE DATABASE IF NOT EXISTS admin_spring_boot
       CHARACTER SET utf8mb4
       COLLATE utf8mb4_general_ci;
-- 进入数据库
USE admin_spring_boot;
#  创建用户表 user 如果表存在则忽略 有 如下字段 name(用户名称) email(用户邮箱)
#  password(用户密码) name设置为唯一索引 email 也设置唯一索引
#  create_time(创建时间) update_time(更新时间)
CREATE TABLE IF NOT EXISTS user
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
# 创建角色表 role 如果表存在则忽略 有 如下字段 name(角色名称)
# description(角色描述)
CREATE TABLE IF NOT EXISTS role
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
#  创建用户角色关联表 user_role 如果表存在则忽略
# 有 如下字段 user_id(用户id) role_id(角色id)
CREATE TABLE IF NOT EXISTS user_role
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

#  创建权限表 permission 如果表存在则忽略
#  按类型存三种数据：1目录 2菜单 3按钮
#  目录：name/path/icon/visible；菜单：name/path/component/icon/is_cache/visible；按钮：name/per_key
#  只有按钮需要 per_key（权限字符），per_key 设置唯一索引
CREATE TABLE IF NOT EXISTS permission
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    parent_id INT NOT NULL DEFAULT 0 COMMENT '父权限ID，0=顶级（仅目录可为0）',
    per_type INT NOT NULL COMMENT '权限类型：1目录 2菜单 3按钮',
    name VARCHAR(50) NOT NULL COMMENT '显示名称（目录名称/菜单名称/按钮权限名称）',
    per_key VARCHAR(100) NULL COMMENT '权限字符（仅按钮必填，如 system:user:add）',
    path VARCHAR(200) NULL COMMENT '路由地址（目录/菜单用）',
    component VARCHAR(200) NULL COMMENT '组件路径（菜单必填，目录可填Layout）',
    icon VARCHAR(100) NULL COMMENT '图标（目录/菜单用）',
    is_cache INT NULL DEFAULT 1 COMMENT '是否缓存页面keep-alive（菜单用）：0否 1是',
    visible INT NULL DEFAULT 1 COMMENT '是否显示（目录/菜单用）：0隐藏 1显示',
    sort_num INT NOT NULL DEFAULT 0 COMMENT '排序号',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_parent_id (parent_id),
    UNIQUE KEY uk_per_key (per_key)
);
#  创建权限角色关联表 permission_role 如果表存在则忽略
#  有 如下字段 permission_id(权限id) role_id(角色id)
CREATE TABLE IF NOT EXISTS permission_role
(
    permission_id INT NOT NULL,
    role_id INT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入角色数据
INSERT INTO role (name, description) VALUES
('super-admin', '超级管理员');

-- 插入用户表数据
INSERT INTO user (name, email, password) VALUES
('slr', 'slr@163.com', '***REDACTED***');

-- 插入用户角色关联数据
INSERT INTO user_role (user_id, role_id) VALUES
    (1, 1);

-- =====================================================================
-- 旧库迁移脚本（仅当数据库里已存在旧版 permission 表时，手动执行一次）
-- 说明：若旧表数据中存在超过新长度限制的字段值，对应 ALTER 会报错，请先清理数据
-- =====================================================================

-- 1. 新增菜单缓存字段
ALTER TABLE permission ADD COLUMN is_cache INT NULL DEFAULT 1 COMMENT '是否缓存页面keep-alive（菜单用）：0否 1是' AFTER icon;

-- 2. 字段改名：per_name -> name
ALTER TABLE permission CHANGE COLUMN per_name name VARCHAR(50) NOT NULL COMMENT '显示名称（目录名称/菜单名称/按钮权限名称）';

-- 3. 放开类型专属字段的非空约束（按钮不需要 path/component/icon/visible；per_key 仅按钮必填）
--    per_key 已有唯一索引，MySQL 唯一索引允许多个 NULL，目录/菜单不填不受影响
ALTER TABLE permission
    MODIFY COLUMN per_key VARCHAR(100) NULL COMMENT '权限字符（仅按钮必填，如 system:user:add）',
    MODIFY COLUMN path VARCHAR(200) NULL COMMENT '路由地址（目录/菜单用）',
    MODIFY COLUMN component VARCHAR(200) NULL COMMENT '组件路径（菜单必填，目录可填Layout）',
    MODIFY COLUMN icon VARCHAR(100) NULL COMMENT '图标（目录/菜单用）',
    MODIFY COLUMN visible INT NULL DEFAULT 1 COMMENT '是否显示（目录/菜单用）：0隐藏 1显示',
    MODIFY COLUMN parent_id INT NOT NULL DEFAULT 0 COMMENT '父权限ID，0=顶级（仅目录可为0）',
    ADD KEY idx_parent_id (parent_id);