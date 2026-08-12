package com.sunsun.adminspringboot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "登录结果响应")
public class LoginResult {

    @Schema(description = "登录令牌", example = "xxxx-xxxx-xxxx")
    private String token;

    @Schema(description = "用户ID", example = "1")
    private Integer userId;

    @Schema(description = "用户名", example = "admin")
    private String name;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "用户角色列表", example = "[\"ADMIN\",\"USER\"]")
    private List<String> role;
}
