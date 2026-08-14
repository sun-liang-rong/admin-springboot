package com.sunsun.adminspringboot.service;

import com.sunsun.adminspringboot.dto.request.DictDataPageQuery;
import com.sunsun.adminspringboot.dto.request.DictDataRequest;
import com.sunsun.adminspringboot.entity.DictData;
import jakarta.validation.Valid;

import java.util.List;

public interface DictDataService {
    List<DictData> getDictData(DictDataPageQuery dictDataPageQuery);

    Integer addDictData(DictDataRequest dictDataRequest);

    Integer updateDictData(DictDataRequest dictDataRequest);

    Integer deleteDictData(Integer id);
}
