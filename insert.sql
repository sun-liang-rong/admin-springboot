-- =====================================================================
-- insert.sql  初始化数据：超级管理员
-- 幂等脚本，可重复执行，不会重复插入
-- 注意：密码为明文存储（与 AuthService 登录校验一致）
-- =====================================================================

USE admin_spring_boot;

-- 1. 超级管理员角色（已存在则忽略）
INSERT INTO role (name, description)
VALUES ('super-admin', '超级管理员')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 2. 超级管理员账号（默认账号 admin / admin123）
--    如需修改账号，改这里即可（name/email 有唯一索引）
INSERT INTO user (name, email, password)
VALUES ('admin', 'admin@admin.com', 'admin123')
ON DUPLICATE KEY UPDATE password = VALUES(password);

-- 3. 关联 admin 用户 -> super-admin 角色（按名称关联，不依赖自增 id）
INSERT IGNORE INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM user u
         JOIN role r ON u.name = 'admin' AND r.name = 'super-admin';

-- 4. 兼容旧账号：若存在 slr 用户，也绑定超级管理员角色（幂等）
INSERT IGNORE INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM user u
         JOIN role r ON u.name = 'slr' AND r.name = 'super-admin'
WHERE NOT EXISTS (SELECT 1 FROM user_role ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

-- 执行后可登录账号：
--   admin  / admin123（超级管理员）
--   slr    / <password-redacted>（超级管理员，如已存在则自动绑定角色）
