package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName("user")
@Data
@Schema(description = "用户实体")
public class User {
//    主键ID
    @Schema(description = "主键ID，自动递增", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;
//    用户名
    @Schema(description = "用户名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("name")
    private String name;
//    邮箱
    @Schema(description = "邮箱", example = "zhangsan@example.com")
    @TableField("email")
    private String email;
    @Schema(description = "密码", example = "123456")
    @TableField("password")
    private String password;
}
