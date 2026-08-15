package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.LoginLogQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.LoginLog;

public interface LoginLogService {

    /** 分页查询登录日志 */
    PageResult<LoginLog> list(LoginLogQuery query);

    /** 记录登录日志（成功/失败） */
    void save(LoginLog log);
}
