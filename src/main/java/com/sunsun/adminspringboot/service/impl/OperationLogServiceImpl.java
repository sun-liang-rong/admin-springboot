package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunsun.adminspringboot.dto.request.OperationLogQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.OperationLog;
import com.sunsun.adminspringboot.mapper.OperationLogMapper;
import com.sunsun.adminspringboot.service.OperationLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Override
    public PageResult<OperationLog> list(OperationLogQuery query) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUserName()), OperationLog::getUserName, query.getUserName())
                .eq(StringUtils.hasText(query.getModule()), OperationLog::getModule, query.getModule())
                .eq(StringUtils.hasText(query.getOperationType()), OperationLog::getOperationType, query.getOperationType())
                .eq(query.getStatus() != null, OperationLog::getStatus, query.getStatus())
                .ge(StringUtils.hasText(query.getStartTime()), OperationLog::getCreateTime, query.getStartTime())
                .le(StringUtils.hasText(query.getEndTime()), OperationLog::getCreateTime, query.getEndTime())
                .orderByDesc(OperationLog::getId);
        Page<OperationLog> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<OperationLog> result = operationLogMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }

    @Override
    public void save(OperationLog log) {
        operationLogMapper.insert(log);
    }
}
