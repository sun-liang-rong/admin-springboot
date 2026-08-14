package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "用户分页查询参数")
public class UserPageQuery {

    @Schema(description = "当前页码", example = "1", defaultValue = "1")
    @Min(value = 1, message = "页码最小为 1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10", defaultValue = "10")
    @Min(value = 1, message = "每页大小最小为 1")
    @Max(value = 100, message = "每页大小最大为 100")
    private Integer pageSize = 10;

    @Schema(description = "用户名（模糊查询）", example = "张三")
    private String name;

    @Schema(description = "邮箱（模糊查询）", example = "zhangsan@example.com")
    private String email;
}