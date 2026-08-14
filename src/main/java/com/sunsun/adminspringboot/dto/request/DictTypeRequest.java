package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "字典类型请求参数")
public class DictTypeRequest {

    @Schema(description = "字典类型ID（修改时必传，新增时不传）", example = "1")
    private Integer id;

    @Schema(description = "字典名称", example = "用户性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @Schema(description = "字典类型编码", example = "sys_user_sex", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "字典类型不能为空")
    private String dictType;

    @Schema(description = "状态（0-禁用 1-启用）", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "系统内置字典类型")
    private String remake;
}
