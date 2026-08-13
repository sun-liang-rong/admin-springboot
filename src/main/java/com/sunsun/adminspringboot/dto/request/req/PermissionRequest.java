package com.sunsun.adminspringboot.dto.request.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "权限请求参数")
public class PermissionRequest {

    @Schema(description = "权限ID（修改时必传，新增时不传）", example = "1")
    private Integer id;

    @Schema(description = "父权限ID，0 表示顶级（仅目录可为0；菜单的父=目录，按钮的父=菜单）", example = "0")
    @NotNull(message = "父权限ID不能为空")
    private Integer parentId;

    @Schema(description = "权限类型：1目录 2菜单 3按钮", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "权限类型不能为空")
    private Integer perType;

    @Schema(description = "显示名称（目录名称/菜单名称/按钮权限名称）", example = "用户管理", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "名称不能为空")
    private String name;

    @Schema(description = "权限字符（仅按钮类型必填，如 system:user:add）", example = "system:user:add")
    private String perKey;

    @Schema(description = "路由地址（目录/菜单用，按钮不填）", example = "/system/user")
    private String path;

    @Schema(description = "组件路径（菜单必填，目录可填Layout，按钮不填）", example = "system/user/index")
    private String component;

    @Schema(description = "图标（目录/菜单用，按钮不填）", example = "user")
    private String icon;

    @Schema(description = "是否缓存页面keep-alive（菜单用）：0否 1是", example = "1")
    private Integer isCache;

    @Schema(description = "是否显示（目录/菜单用）：0隐藏 1显示", example = "1")
    private Integer visible;

    @Schema(description = "排序编号", example = "1")
    private Integer sortNum;

    @Schema(description = "状态：0禁用 1启用", example = "1")
    private Integer status;

    @Schema(description = "创建时间（系统自动生成，请求无需传）", hidden = true)
    private String createTime;

    @Schema(description = "更新时间（系统自动生成，请求无需传）", hidden = true)
    private String updateTime;
}
