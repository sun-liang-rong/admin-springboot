package com.sunsun.adminspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunsun.adminspringboot.entity.UserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Insert("<script>" +
            "INSERT INTO user_role (user_id, role_id) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.userId}, #{item.roleId})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("list") List<UserRole> userRole);

    // 根据用户ID查询角色名称集合（Sa-Token 鉴权用）
    @Select("SELECT r.name FROM user u INNER JOIN user_role ur ON u.id = ur.user_id INNER JOIN role r ON ur.role_id = r.id WHERE u.id = #{userId}")
    List<String> selectUserRoleKeys(@Param("userId") Long userId);

    // 根据用户ID查询角色ID集合（用于管理端分配角色时回显）
    @Select("SELECT DISTINCT r.id FROM user u INNER JOIN user_role ur ON u.id = ur.user_id INNER JOIN role r ON ur.role_id = r.id WHERE u.id = #{userId}")
    List<Integer> selectUserRoleIds(@Param("userId") Long userId);
}
