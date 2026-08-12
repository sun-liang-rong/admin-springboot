package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "角色实体")
public class Role {

    @Schema(description = "角色ID，自动递增", example = "1")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Schema(description = "角色名", example = "管理员", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "角色描述", example = "系统管理员，拥有所有权限")
    private String description;
}
