package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.DictTypePageQuery;
import com.sunsun.adminspringboot.dto.request.DictTypeRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.DictType;
import jakarta.validation.Valid;

public interface DictTypeService {
    PageResult<DictType> getDictTypeList(DictTypePageQuery dictTypePageQuery);

    Integer addDictType(DictTypeRequest dictTypeRequest);

    Integer updateDictType(@Valid DictTypeRequest dictTypeRequest);

    Integer deleteDictType(Integer id);
}
