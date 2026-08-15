package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("operation_log")
@Schema(description = "操作日志实体")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "操作人ID")
    private Integer userId;

    @Schema(description = "操作人用户名")
    private String userName;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "操作描述")
    private String operation;

    @Schema(description = "操作类型（INSERT/UPDATE/DELETE/QUERY/LOGIN/LOGOUT/OTHER）")
    private String operationType;

    @Schema(description = "请求方式（GET/POST/...）")
    private String requestMethod;

    @Schema(description = "请求地址")
    private String requestUrl;

    @Schema(description = "请求参数")
    private String requestParams;

    @Schema(description = "请求IP")
    private String ip;

    @Schema(description = "耗时（毫秒）")
    private Long durationMs;

    @Schema(description = "状态（1成功 0失败）")
    private Integer status;

    @Schema(description = "错误信息")
    private String errorMsg;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private String createTime;
}
