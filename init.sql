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
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

#  创建权限表 permission 如果表存在则忽略 有 如下字段 parent_id(父权限ID)
#  per_name(权限名称) per_key(权限标识 例如：user:add)
#  per_type(权限类型 1目录 2菜单 3按钮) path(前端路由地址)
#  component(前端组件地址) icon(菜单图标) sort_num(排序号)
#  visible(是否显示 0不显示 1显示) status(状态 0禁用 1启用)
#  create_time(创建时间) update_time(更新时间) per_key设置为唯一索引
CREATE TABLE IF NOT EXISTS permission
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    parent_id INT NOT NULL,
    per_name VARCHAR(255) NOT NULL,
    per_key VARCHAR(255) NOT NULL UNIQUE,
    per_type INT NOT NULL,
    path VARCHAR(255) NOT NULL,
    component VARCHAR(255) NOT NULL,
    icon VARCHAR(255) NOT NULL,
    sort_num INT NOT NULL,
    visible INT NOT NULL,
    status INT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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