package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunsun.adminspringboot.dto.request.LoginLogQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.LoginLog;
import com.sunsun.adminspringboot.mapper.LoginLogMapper;
import com.sunsun.adminspringboot.service.LoginLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public PageResult<LoginLog> list(LoginLogQuery query) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUserName()), LoginLog::getUserName, query.getUserName())
                .eq(query.getStatus() != null, LoginLog::getStatus, query.getStatus())
                .ge(StringUtils.hasText(query.getStartTime()), LoginLog::getLoginTime, query.getStartTime())
                .le(StringUtils.hasText(query.getEndTime()), LoginLog::getLoginTime, query.getEndTime())
                .orderByDesc(LoginLog::getId);
        Page<LoginLog> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<LoginLog> result = loginLogMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }

    @Override
    public void save(LoginLog log) {
        loginLogMapper.insert(log);
    }
}
