package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "字典数据请求参数")
public class DictDataRequest {
    @Schema(description = "字典数据ID（修改时必传，新增时不传）", example = "1")
    private Integer id;

    @Schema(description = "字典类型编码", example = "sys_user_sex", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典类型不能为空")
    private String dictType;

    @Schema(description = "字典标签", example = "男", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典标签不能为空")
    private String dictLabel;

    @Schema(description = "字典值", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典值不能为空")
    private String dictValue;

    @Schema(description = "排序号（越小越靠前）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典排序不能为空")
    private Integer sort;

    @Schema(description = "状态（0-禁用 1-启用）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典状态不能为空")
    private Integer status;
}
