package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.OperationLogQuery;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.OperationLog;

public interface OperationLogService {

    /** 分页查询操作日志 */
    PageResult<OperationLog> list(OperationLogQuery query);

    /** 保存操作日志 */
    void save(OperationLog log);
}
