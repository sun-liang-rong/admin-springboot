package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("login_log")
@Schema(description = "登录日志实体")
public class LoginLog {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "登录用户名")
    private String userName;

    @Schema(description = "登录IP")
    private String ip;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "状态（1成功 0失败）")
    private Integer status;

    @Schema(description = "提示信息（如：登录成功 / 用户名或密码错误）")
    private String message;

    @TableField("login_time")
    @Schema(description = "登录时间")
    private String loginTime;
}
