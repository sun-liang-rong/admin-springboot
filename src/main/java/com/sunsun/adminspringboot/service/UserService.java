package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.query.UserPageQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.entity.User;

import java.util.List;

public interface UserService {
    PageResult<User> list(UserPageQuery userPageQuery);
    List<PermissionListResult> getMenu(Integer userId);

    List<String> getPermission(Integer userId);
}
