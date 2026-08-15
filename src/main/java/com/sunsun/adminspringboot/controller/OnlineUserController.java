package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.response.OnlineUserResult;
import com.sunsun.adminspringboot.service.OnlineUserService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("monitor/online")
@Tag(name = "在线用户模块", description = "在线会话查询与强制下线")
public class OnlineUserController {

    @Resource
    private OnlineUserService onlineUserService;

    @Operation(summary = "查询在线用户列表", description = "返回所有在线会话，可按用户名关键字过滤")
    @SaCheckPermission("system:onlineUser:list")
    @GetMapping("/list")
    public ApiResponse<List<OnlineUserResult>> list(
            @Parameter(description = "用户名关键字（可选）") @RequestParam(required = false) String keyword) {
        return ApiResponse.success(onlineUserService.list(keyword));
    }

    @Operation(summary = "强制下线", description = "根据 token 强制指定会话下线")
    @SaCheckPermission("system:onlineUser:kick")
    @DeleteMapping("/kick/{token}")
    public ApiResponse<Void> kick(
            @Parameter(description = "Token", required = true) @PathVariable String token) {
        onlineUserService.kick(token);
        return ApiResponse.success(null);
    }
}
