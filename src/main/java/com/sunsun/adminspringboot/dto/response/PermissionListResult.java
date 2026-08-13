package com.sunsun.adminspringboot.dto.response;

import com.sunsun.adminspringboot.entity.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "权限树节点响应")
public class PermissionListResult {

    @Schema(description = "权限ID", example = "1")
    private Integer id;

    @Schema(description = "父权限ID，0 表示顶级", example = "0")
    private Integer parentId;

    @Schema(description = "权限类型：1目录 2菜单 3按钮", example = "2")
    private Integer perType;

    @Schema(description = "显示名称（目录名称/菜单名称/按钮权限名称）", example = "用户管理")
    private String name;

    @Schema(description = "权限字符（仅按钮有值）", example = "system:user:add")
    private String perKey;

    @Schema(description = "路由地址（目录/菜单用）", example = "/system/user")
    private String path;

    @Schema(description = "组件路径（菜单用）", example = "system/user/index")
    private String component;

    @Schema(description = "图标（目录/菜单用）", example = "user")
    private String icon;

    @Schema(description = "是否缓存页面keep-alive（菜单用）：0否 1是", example = "1")
    private Integer isCache;

    @Schema(description = "是否显示（目录/菜单用）：0隐藏 1显示", example = "1")
    private Integer visible;

    @Schema(description = "排序编号", example = "1")
    private Integer sortNum;

    @Schema(description = "状态：0禁用 1启用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", example = "2025-01-01 12:00:00")
    private String createTime;

    @Schema(description = "更新时间", example = "2025-01-01 12:00:00")
    private String updateTime;

    @Schema(description = "子权限节点列表（树形结构）")
    private List<PermissionListResult> children;

    /**
     * Permission 实体 转 响应 DTO（两个 Service 共用）
     */
    public static PermissionListResult of(Permission permission) {
        PermissionListResult dto = new PermissionListResult();
        dto.setId(permission.getId());
        dto.setParentId(permission.getParentId());
        dto.setPerType(permission.getPerType());
        dto.setName(permission.getName());
        dto.setPerKey(permission.getPerKey());
        dto.setPath(permission.getPath());
        dto.setComponent(permission.getComponent());
        dto.setIcon(permission.getIcon());
        dto.setIsCache(permission.getIsCache());
        dto.setVisible(permission.getVisible());
        dto.setSortNum(permission.getSortNum());
        dto.setStatus(permission.getStatus());
        dto.setCreateTime(permission.getCreateTime());
        dto.setUpdateTime(permission.getUpdateTime());
        return dto;
    }
}
