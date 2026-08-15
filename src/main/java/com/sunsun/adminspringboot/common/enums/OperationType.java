package com.sunsun.adminspringboot.common.enums;

/**
 * 操作日志类型
 */
public enum OperationType {
    /** 新增 */
    INSERT("新增"),
    /** 修改 */
    UPDATE("修改"),
    /** 删除 */
    DELETE("删除"),
    /** 查询 */
    QUERY("查询"),
    /** 登录 */
    LOGIN("登录"),
    /** 退出 */
    LOGOUT("退出"),
    /** 其他 */
    OTHER("其他");

    private final String label;

    OperationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
