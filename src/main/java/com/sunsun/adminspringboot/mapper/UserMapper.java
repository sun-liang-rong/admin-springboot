package com.sunsun.adminspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunsun.adminspringboot.entity.Permission;
import com.sunsun.adminspringboot.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT DISTINCT p.per_key FROM user u INNER JOIN user_role ur ON u.id = ur.user_id INNER JOIN permission_role pr ON ur.role_id = pr.role_id INNER JOIN permission p ON p.id = pr.permission_id WHERE u.id = #{userId} AND p.per_type = 3")
    List<String> selectUserPermissionKeys(@Param("userId") Long userId);
    // 根据用户ID 查询用户的菜单列表（目录+菜单），按 sort_num 排序
    @Select("SELECT DISTINCT p.* FROM user u INNER JOIN user_role ur ON u.id = ur.user_id INNER JOIN permission_role pr ON ur.role_id = pr.role_id INNER JOIN permission p ON p.id = pr.permission_id WHERE u.id = #{userId} AND p.per_type IN (1, 2) ORDER BY p.sort_num")
    List<Permission> selectUserMenuKeys(@Param("userId") Long userId);
}
