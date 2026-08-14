# admin-springboot

基于 **Spring Boot 4 + MyBatis-Plus 3 + Sa-Token** 的后台管理系统后端，提供用户、角色、权限、字典类型、字典数据等基础管理模块。

## 技术栈

| 分类 | 技术 | 版本 |
|---|---|---|
| 框架 | Spring Boot | 4.1.0 |
| 语言 | Java | 17 |
| ORM | MyBatis-Plus（含分页插件） | 3.5.17 |
| 数据库 | MySQL | 8.0 |
| 连接池 | Druid | 1.2.28 |
| 认证授权 | Sa-Token（JWT 风格 Token） | 1.45.0 |
| 接口文档 | springdoc-openapi（Swagger UI） | 3.1.0 |
| 参数校验 | Jakarta Validation | - |

## 快速开始

### 1. 初始化数据库

```bash
# 建表（幂等，可重复执行；纯建表，无数据）
mysql -uroot -p < init.sql

# 初始化超级管理员（admin / admin123，幂等）
mysql -uroot -p < insert.sql
```

> `init.sql` 中 `permission` 表如存在负数/超大 id（历史数据溢出导致），需先执行 `repair_permission_ids.sql` 重排，再使用。

### 2. 修改数据库连接

编辑 `src/main/resources/application.yaml` 中的 `spring.datasource` 配置。

### 3. 启动

```bash
./mvnw spring-boot:run
```

- 接口文档：<http://localhost:8080/api/swagger-ui.html>
- 默认账号：`admin / admin123`

## 目录结构

```
admin-springboot
├── src/main/java/com/sunsun/adminspringboot
│   ├── AdminSpringbootApplication.java   # 启动类（@SpringBootApplication + @MapperScan）
│   ├── common/                           # 通用模块
│   │   ├── ApiResponse.java              #   统一响应结果封装（code/message/data/traceId/timestamp）
│   │   ├── TraceContext.java             #   链路追踪上下文（TraceId 管理）
│   │   ├── enums/                        #   枚举定义
│   │   │   └── PermissionTypeEnum.java   #     权限类型枚举（1目录 2菜单 3按钮）
│   │   └── exception/                    #   异常体系
│   │       ├── BusinessException.java    #     业务异常（可携带业务错误码）
│   │       └── GlobalExceptionHandler.java  # 全局异常处理器（统一转 ApiResponse）
│   ├── config/                           # 配置类
│   │   ├── MybatisPlusConfig.java        #   MyBatis-Plus 分页插件
│   │   ├── OpenApiConfiguration.java     #   springdoc 接口文档配置
│   │   └── SaTokenConfigure.java         #   Sa-Token 全局过滤器（登录鉴权/权限校验/安全响应头）
│   ├── controller/                       # 控制层（接收请求，参数校验，返回 ApiResponse）
│   │   ├── AuthController.java           #   登录、注册
│   │   ├── UserController.java           #   用户管理
│   │   ├── RoleController.java           #   角色管理
│   │   ├── PermissionController.java     #   权限管理（目录/菜单/按钮）
│   │   ├── UserRoleController.java       #   用户角色绑定
│   │   ├── RolePermissionController.java #   角色权限授权
│   │   ├── DictTypeController.java       #   字典类型管理
│   │   └── DictDataController.java       #   字典数据管理
│   ├── dto/                              # 数据传输对象
│   │   ├── request/                      #   请求 DTO（query 参数与请求体统一平铺）
│   │   │   ├── LoginRequest.java         #     登录请求（name/password）
│   │   │   ├── RegisterRequest.java      #     注册请求
│   │   │   ├── UserPageQuery.java        #     用户分页查询参数
│   │   │   ├── UserRoleRequest.java      #     用户-角色绑定请求
│   │   │   ├── RolePageQuery.java        #     角色分页查询参数
│   │   │   ├── RoleRequest.java          #     角色新增/修改请求
│   │   │   ├── RolePermissionRequest.java #    角色-权限授权请求
│   │   │   ├── PermissionRequest.java    #     权限新增/修改请求
│   │   │   ├── DictTypePageQuery.java    #     字典类型分页查询参数
│   │   │   ├── DictTypeRequest.java      #     字典类型新增/修改请求
│   │   │   ├── DictDataPageQuery.java    #     字典数据查询参数
│   │   │   └── DictDataRequest.java      #     字典数据新增/修改请求
│   │   └── response/                     #   响应 DTO
│   │       ├── PageResult.java           #     分页结果通用封装（data/total/pageNum/pageSize/pages）
│   │       ├── LoginResult.java          #     登录结果（token/用户信息/角色）
│   │       └── PermissionListResult.java #     权限树/权限列表结果
│   ├── entity/                           # 数据库实体（与表一一对应，@TableName/@TableField 映射）
│   │   ├── User.java                     #   用户表 user
│   │   ├── Role.java                     #   角色表 role
│   │   ├── UserRole.java                 #   用户角色关联表 user_role
│   │   ├── Permission.java               #   权限表 permission（目录/菜单/按钮）
│   │   ├── RolePermission.java           #   权限角色关联表 permission_role
│   │   ├── DictType.java                 #   字典类型表 dict_type
│   │   └── DictData.java                 #   字典数据表 dict_data
│   ├── mapper/                           # 持久层（MyBatis-Plus Mapper 接口，继承 BaseMapper）
│   │   ├── UserMapper.java / RoleMapper.java / PermissionMapper.java
│   │   ├── UserRoleMapper.java / RolePermissionMapper.java
│   │   └── DictTypeMapper.java / DictDataMapper.java
│   ├── security/                         # 安全认证模块
│   │   └── StpInterfaceImpl.java         #   Sa-Token 权限数据源实现（用户角色/权限加载）
│   ├── service/                          # 业务层（接口定义）
│   │   ├── AuthService.java / UserService.java / RoleService.java / PermissionService.java
│   │   ├── UserRoleService.java / RolePermissionService.java
│   │   └── DictTypeService.java / DictDataService.java
│   ├── service/impl/                     # 业务层实现
│   │   └── *ServiceImpl.java             #   各业务接口实现（含 MyBatis-Plus 条件查询/事务逻辑）
│   └── util/                             # 通用工具类
│       └── PermissionTreeBuilder.java    #   权限树构建工具（目录-菜单-按钮层级组装）
├── src/main/resources
│   └── application.yaml                  # 应用配置（数据源/Sa-Token/springdoc）
├── init.sql                              # 数据库建表脚本（幂等，纯建表）
├── insert.sql                            # 初始化数据脚本（超级管理员 admin/admin123）
├── repair_permission_ids.sql             # 权限表 id 重排脚本（修复负数/超大 id）
└── pom.xml                               # Maven 依赖配置
```

## 模块职责说明

| 模块 | 职责 |
|---|---|
| `common` | 统一响应封装、全局异常处理、链路追踪、枚举常量。所有接口返回 `ApiResponse` |
| `config` | 第三方组件装配：MyBatis-Plus 分页、Sa-Token 鉴权过滤器、OpenAPI 文档 |
| `controller` | 只做"接收参数 → 参数校验 → 调 service → 返回 ApiResponse"，不含业务逻辑 |
| `dto/request` | 请求参数对象（含 Jakarta Validation 校验注解，如 `@NotBlank`、`@Min`） |
| `dto/response` | 响应对象（分页封装、登录结果等） |
| `entity` | 与数据库表字段一一对应的实体类 |
| `mapper` | 数据访问层，继承 `BaseMapper<T>` 获得 CRUD，复杂查询在 Mapper 接口中声明 |
| `service` / `impl` | 业务逻辑层。接口定义契约，实现类写具体业务（条件组装、事务、异常抛出） |
| `security` | Sa-Token 权限数据源实现（登录后按用户加载角色、按钮权限） |
| `util` | 无状态通用工具（权限树构建等） |

## 分层调用约定

```
Controller（参数校验）→ Service 接口 → ServiceImpl（业务逻辑）→ Mapper（SQL）→ MySQL
                                        ↘ entity / dto
```

- 所有接口返回统一 `ApiResponse<T>`（`code=200` 成功，业务异常返回 `code!=200`）
- 分页查询使用 MyBatis-Plus 分页插件，返回 `PageResult<T>`
- 认证：请求头携带 `Authorization: Bearer <token>`（Sa-Token）
- 除 `/auth/login`、`/auth/register`、接口文档外，所有接口均需登录

## 数据库说明

- 库名：`admin_spring_boot`（utf8mb4）
- 所有表**不使用外键**，关联关系由应用层维护
- 7 张表：`user`、`role`、`user_role`、`permission`、`permission_role`、`dict_type`、`dict_data`
