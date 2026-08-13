package com.sunsun.adminspringboot.dto.request.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户角色修改请求参数")
public class UserRoleRequest {

    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @Schema(description = "角色ID列表，传空数组表示清空该用户所有角色", example = "[1, 2]")
    private List<Integer> roleList;
}
