package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@TableName("dict_data")
@Schema(description = "字典数据实体")
public class DictData {
    @TableId(type = IdType.AUTO)
    @Schema(description = "字典数据ID（自增）", example = "1")
    private Integer id;

    @TableField("dict_type")
    @Schema(description = "字典类型编码", example = "sys_user_sex")
    private String dictType;

    @TableField("dict_label")
    @Schema(description = "字典标签", example = "男")
    private String dictLabel;

    @TableField("dict_value")
    @Schema(description = "字典值", example = "0")
    private String dictValue;

    @Schema(description = "排序号", example = "1")
    private Integer sort;

    @Schema(description = "状态（0-禁用 1-启用）", example = "1")
    private Integer status;

    @TableField("create_time")
    @Schema(description = "创建时间", example = "2025-01-01 12:00:00")
    private String createTime;

    @TableField("update_time")
    @Schema(description = "更新时间", example = "2025-01-01 12:00:00")
    private String updateTime;
}
