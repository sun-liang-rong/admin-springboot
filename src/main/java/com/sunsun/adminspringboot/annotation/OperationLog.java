package com.sunsun.adminspringboot.annotation;

import com.sunsun.adminspringboot.common.enums.OperationType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解：标注在 Controller 方法上，由 {@code OperationLogAspect} 拦截记录
 * <p>用法：@OperationLog(module = "用户管理", operation = "分配角色", type = OperationType.UPDATE)</p>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /** 所属模块（如：用户管理 / 角色管理 / 字典管理） */
    String module() default "";

    /** 操作描述（如：新增用户 / 删除角色） */
    String operation() default "";

    /** 操作类型 */
    OperationType type() default OperationType.OTHER;
}
