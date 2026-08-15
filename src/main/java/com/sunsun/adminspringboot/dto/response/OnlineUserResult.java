package com.sunsun.adminspringboot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "在线用户信息")
public class OnlineUserResult {

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "Token（截断展示）")
    private String token;

    @Schema(description = "登录时间")
    private String loginTime;

    @Schema(description = "登录IP")
    private String loginIp;

    @Schema(description = "剩余有效时间（秒）")
    private Long timeout;
}
