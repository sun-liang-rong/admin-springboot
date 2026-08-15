package com.sunsun.adminspringboot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "当前用户信息（个人中心）")
public class ProfileResult {

    @Schema(description = "用户ID", example = "1")
    private Integer userId;

    @Schema(description = "用户名", example = "admin")
    private String name;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "角色列表", example = "[\"super-admin\"]")
    private List<String> roles;
}
