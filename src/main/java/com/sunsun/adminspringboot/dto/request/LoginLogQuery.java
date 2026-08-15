package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录日志分页查询参数")
public class LoginLogQuery {

    @Schema(description = "当前页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "登录用户名（模糊）")
    private String userName;

    @Schema(description = "状态（1成功 0失败）")
    private Integer status;

    @Schema(description = "开始时间（yyyy-MM-dd HH:mm:ss）")
    private String startTime;

    @Schema(description = "结束时间（yyyy-MM-dd HH:mm:ss）")
    private String endTime;
}
