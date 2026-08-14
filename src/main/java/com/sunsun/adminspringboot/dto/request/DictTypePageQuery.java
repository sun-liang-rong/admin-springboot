package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "字典类型分页查询参数")
public class DictTypePageQuery {

    @Schema(description = "字典类型编码（精确匹配）", example = "sys_user_sex")
    private String dictType;

    @Schema(description = "字典类型名称（精确匹配）", example = "用户性别")
    private String dictName;

    @Schema(description = "状态（0-禁用 1-启用，精确匹配）", example = "1")
    private Integer status;

    @Schema(description = "当前页码", example = "1", defaultValue = "1")
    // 最小0
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10", defaultValue = "10")
    // 最大100
    @Max(value = 100, message = "页大小不能大于100")
    @Min(value = 1, message = "页大小不能小于1")
    private Integer pageSize = 10;
}
