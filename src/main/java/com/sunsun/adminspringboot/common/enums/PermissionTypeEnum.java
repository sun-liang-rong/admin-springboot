package com.sunsun.adminspringboot.common.enums;

import lombok.Getter;

/**
 * 权限类型枚举
 * <p>per_type 取值：1目录 2菜单 3按钮，与数据库注释约定一致</p>
 */
@Getter
public enum PermissionTypeEnum {

    DIRECTORY(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮");

    private final int code;
    private final String desc;

    PermissionTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 获取枚举，非法值返回 null
     */
    public static PermissionTypeEnum of(Integer code) {
        if (code == null) {
            return null;
        }
        for (PermissionTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
