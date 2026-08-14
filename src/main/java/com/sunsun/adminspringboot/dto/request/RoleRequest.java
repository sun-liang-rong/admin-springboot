package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "角色请求参数")
public class RoleRequest {

    @Schema(description = "角色ID（修改时必传，新增时不传）", example = "1")
    private Integer id;

    @Schema(description = "角色名", example = "管理员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色名不能为空")
    private String name;

    @Schema(description = "角色描述", example = "系统管理员，拥有所有权限")
    private String description;
}
