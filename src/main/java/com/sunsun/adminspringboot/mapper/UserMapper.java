package com.sunsun.adminspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunsun.adminspringboot.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    // 根据用户ID查询角色集合 需要进行关联查询 查询关联表 在查出对应的角色集合
    @Select("SELECT r.name FROM user u INNER JOIN  user_role ur ON u.id = ur.user_id INNER JOIN role r ON ur.role_id = r.id WHERE u.id = #{userId} ")
    List<String> selectUserRoleKeys(@Param("userId") Long userId);
    @Select("SELECT p.per_key FROM user u INNER JOIN user_role ur ON u.id = ur.user_id INNER JOIN permission_role pr ON ur.id = pr.role_id INNER JOIN permission p ON p.id = pr.permission_id WHERE u.id = #{userId}")
    List<String> selectUserPermissionKeys(@Param("userId") Long userId);
}
