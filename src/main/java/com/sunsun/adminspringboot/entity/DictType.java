package com.sunsun.adminspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dict_type")
public class DictType {
    private Integer id;
    @TableField("dict_type")
    private String dictType;
    @TableField("dict_name")
    private String dictName;
    private Integer status;
    private String remake;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;
}
