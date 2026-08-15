package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.DictTypePageQuery;
import com.sunsun.adminspringboot.dto.request.DictTypeRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.DictData;
import com.sunsun.adminspringboot.entity.DictType;
import com.sunsun.adminspringboot.mapper.DictDataMapper;
import com.sunsun.adminspringboot.mapper.DictTypeMapper;
import com.sunsun.adminspringboot.service.DictTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictTypeServiceImpl implements DictTypeService {
    @Resource
    private DictTypeMapper dictTypeMapper;
    @Resource
    private DictDataMapper dictDataMapper;
    @Override
    public PageResult<DictType> getDictTypeList(DictTypePageQuery dictTypePageQuery) {
        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<DictType>();
        if (StringUtils.hasText(dictTypePageQuery.getDictType())) {
            wrapper.eq(DictType::getDictType, dictTypePageQuery.getDictType());
        }
        if (dictTypePageQuery.getStatus() != null) {
            wrapper.eq(DictType::getStatus, dictTypePageQuery.getStatus());
        }
        if (StringUtils.hasText(dictTypePageQuery.getDictName())) {
            wrapper.like(DictType::getDictName, dictTypePageQuery.getDictName());
        }
        Page<DictType> page = new Page<>(dictTypePageQuery.getPageNum(), dictTypePageQuery.getPageSize());
        // 打印
        System.out.println(page);
        IPage<DictType> dictTypePage = dictTypeMapper.selectPage(page, wrapper);
        System.out.println(dictTypePage);
        return PageResult.of(dictTypePage);
    }

    @Override
    public Integer addDictType(DictTypeRequest dictTypeRequest) {
        try {
            DictType dictType = new DictType();
            dictType.setDictName(dictTypeRequest.getDictName());
            dictType.setDictType(dictTypeRequest.getDictType());
            dictType.setStatus(dictTypeRequest.getStatus());
            dictType.setRemake(dictTypeRequest.getRemake());
            return dictTypeMapper.insert(dictType);
        } catch (Exception e) {
            throw new BusinessException("添加字典类型失败");
        }
    }

    @Override
    public Integer updateDictType(DictTypeRequest dictTypeRequest) {
        try {
            DictType dictType = new DictType();
            dictType.setId(dictTypeRequest.getId());
            dictType.setDictName(dictTypeRequest.getDictName());
            dictType.setDictType(dictTypeRequest.getDictType());
            dictType.setStatus(dictTypeRequest.getStatus());
            dictType.setRemake(dictTypeRequest.getRemake());
            return dictTypeMapper.updateById(dictType);
        } catch (Exception e) {
            throw new BusinessException("修改字典类型失败");
        }
    }

    @Override
    public Integer deleteDictType(Integer id) {
        try {
            return dictTypeMapper.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("删除字典类型失败");
        }
    }

    @Override
    public Map<String, List<DictData>> getAllDict() {
        // 1. 查询所有启用的字典类型（保持创建顺序）
        List<DictType> types = dictTypeMapper.selectList(
                new LambdaQueryWrapper<DictType>()
                        .eq(DictType::getStatus, 1)
                        .orderByAsc(DictType::getId));
        if (types.isEmpty()) {
            // 没有启用的字典类型，返回空 Map
            return Collections.emptyMap();
        }
        List<String> typeCodes = types.stream().map(DictType::getDictType).toList();

        // 2. 一次性查出这些类型下所有启用的字典数据（按 sort、id 排序保证下拉顺序稳定）
        List<DictData> dataList = dictDataMapper.selectList(
                new LambdaQueryWrapper<DictData>()
                        .in(DictData::getDictType, typeCodes)
                        .eq(DictData::getStatus, 1)
                        .orderByAsc(DictData::getSort)
                        .orderByAsc(DictData::getId));

        // 3. 按类型编码分组，保留类型定义顺序
        Map<String, List<DictData>> dictMap = new LinkedHashMap<>();
        for (DictType type : types) {
            dictMap.put(type.getDictType(), new ArrayList<>());
        }
        //
        for (DictData data : dataList) {
            //
            dictMap.computeIfAbsent(data.getDictType(), k -> new ArrayList<>()).add(data);
        }
        return dictMap;
    }

    @Override
    public List<DictData> getDictByType(String dictType) {
        return dictDataMapper.selectList(
                new LambdaQueryWrapper<DictData>()
                        .eq(DictData::getDictType, dictType)
                        .eq(DictData::getStatus, 1)
                        .orderByAsc(DictData::getSort)
                        .orderByAsc(DictData::getId));
    }
}
