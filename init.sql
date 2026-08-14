-- =====================================================================
-- admin_spring_boot 数据库初始化脚本（纯建表，无数据插入）
-- 适用：全新环境初始化 / 已有库结构对齐（幂等，可重复执行，不丢数据）
-- 说明：
--   1. 所有表使用 CREATE TABLE IF NOT EXISTS，已有表不会被重建
--   2. 结构修复段会为旧版关联表补齐 id 主键、唯一约束（不涉及数据）
--   3. permission 表如存在负数/超大 id，请使用 repair_permission_ids.sql 重排
--   4. 所有表不使用外键约束，关联关系由应用层维护（MyBatis-Plus 等）
-- =====================================================================

-- 创建数据库（已存在则忽略）
CREATE DATABASE IF NOT EXISTS admin_spring_boot
       CHARACTER SET utf8mb4
       COLLATE utf8mb4_general_ci;

USE admin_spring_boot;

-- =====================================================================
-- 1. 建表
-- =====================================================================

-- 用户表
CREATE TABLE IF NOT EXISTS user
(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY name (name),
    UNIQUE KEY email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS role
(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS user_role
(
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表';

-- 权限表（1目录 2菜单 3按钮）
CREATE TABLE IF NOT EXISTS permission
(
    id INT NOT NULL AUTO_INCREMENT,
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
    PRIMARY KEY (id),
    UNIQUE KEY uk_per_key (per_key),
    KEY idx_parent_id (parent_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限表';

-- 权限角色关联表
CREATE TABLE IF NOT EXISTS permission_role
(
    id INT NOT NULL AUTO_INCREMENT,
    permission_id INT NOT NULL,
    role_id INT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_permission_role (permission_id, role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '权限角色关联表';

-- 字典类型表
CREATE TABLE IF NOT EXISTS dict_type
(
    id INT NOT NULL AUTO_INCREMENT,
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_name VARCHAR(50) NOT NULL COMMENT '字典名称',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
    remake VARCHAR(255) NULL COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY dict_type (dict_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典类型表';

-- 字典数据表
CREATE TABLE IF NOT EXISTS dict_data
(
    id INT NOT NULL AUTO_INCREMENT,
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_label VARCHAR(50) NOT NULL COMMENT '字典展示中文',
    dict_value VARCHAR(50) NOT NULL COMMENT '字典值',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_dict_type (dict_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典数据表';

-- =====================================================================
-- 2. 结构修复段（幂等，兼容旧版表结构，不涉及数据）
-- =====================================================================

-- 2.1 旧版 user_role / permission_role 缺少 id 主键，补齐
--     （MySQL 8.0 无 ADD COLUMN IF NOT EXISTS，用 information_schema 判断 + PREPARE 动态执行）
SET @cnt = (SELECT COUNT(*) FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'user_role' AND column_name = 'id');
SET @sql = IF(@cnt = 0,
              'ALTER TABLE user_role ADD COLUMN id INT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST',
              'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @cnt = (SELECT COUNT(*) FROM information_schema.columns
            WHERE table_schema = DATABASE() AND table_name = 'permission_role' AND column_name = 'id');
SET @sql = IF(@cnt = 0,
              'ALTER TABLE permission_role ADD COLUMN id INT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST',
              'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2.2 补唯一约束（避免重复绑定角色 / 重复授权）
SET @cnt = (SELECT COUNT(*) FROM information_schema.statistics
            WHERE table_schema = DATABASE() AND table_name = 'user_role' AND index_name = 'uk_user_role');
SET @sql = IF(@cnt = 0,
              'ALTER TABLE user_role ADD UNIQUE KEY uk_user_role (user_id, role_id)',
              'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @cnt = (SELECT COUNT(*) FROM information_schema.statistics
            WHERE table_schema = DATABASE() AND table_name = 'permission_role' AND index_name = 'uk_permission_role');
SET @sql = IF(@cnt = 0,
              'ALTER TABLE permission_role ADD UNIQUE KEY uk_permission_role (permission_id, role_id)',
              'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2.3 修正 AUTO_INCREMENT 计数器
--     注意：ALTER ... AUTO_INCREMENT = 1 是安全的——有数据时自动跳到 max(id)+1，无数据时从 1 开始
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE role AUTO_INCREMENT = 1;
ALTER TABLE dict_data AUTO_INCREMENT = 1;

-- 2.4 清理 dict_type 负数 id（无任何表引用该 id，可安全清理）
--     如需保留数据，请注释掉下面两行
DELETE FROM dict_type WHERE id < 0;
ALTER TABLE dict_type AUTO_INCREMENT = 1;
