package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.DictTypePageQuery;
import com.sunsun.adminspringboot.dto.request.DictTypeRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.DictData;
import com.sunsun.adminspringboot.entity.DictType;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface DictTypeService {
    PageResult<DictType> getDictTypeList(DictTypePageQuery dictTypePageQuery);

    Integer addDictType(DictTypeRequest dictTypeRequest);

    Integer updateDictType(@Valid DictTypeRequest dictTypeRequest);

    Integer deleteDictType(Integer id);

    /**
     * 获取所有启用的字典（类型编码 -> 字典数据列表），供前端全局加载使用
     */
    Map<String, List<DictData>> getAllDict();

    /**
     * 根据字典类型编码查询启用的字典数据列表
     */
    List<DictData> getDictByType(String dictType);
}
