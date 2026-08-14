package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "字典数据查询参数")
public class DictDataPageQuery {
    @Schema(description = "字典类型编码（必传）", example = "sys_user_sex", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典类型不能为空")
    private String dictType;

    @Schema(description = "字典标签（精确匹配）", example = "男")
    private String dictLabel;

    @Schema(description = "状态（0-禁用 1-启用，精确匹配）", example = "1")
    private Integer status;
}
