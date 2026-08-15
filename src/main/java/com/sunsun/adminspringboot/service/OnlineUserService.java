package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.response.OnlineUserResult;

import java.util.List;

public interface OnlineUserService {

    /** 查询在线用户列表（可按用户名关键字过滤） */
    List<OnlineUserResult> list(String keyword);

    /** 强制指定 token 下线 */
    void kick(String tokenValue);
}
