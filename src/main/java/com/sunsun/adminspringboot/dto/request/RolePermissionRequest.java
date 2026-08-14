package com.sunsun.adminspringboot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色权限修改请求参数")
public class RolePermissionRequest {

    @Schema(description = "角色ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    @Schema(description = "权限ID列表，传空数组表示清空该角色所有权限", example = "[1, 2, 3]")
    private List<Integer> permissionList;
}
