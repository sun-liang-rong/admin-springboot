package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.DictTypePageQuery;
import com.sunsun.adminspringboot.dto.request.DictTypeRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.DictType;
import com.sunsun.adminspringboot.mapper.DictTypeMapper;
import com.sunsun.adminspringboot.service.DictTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DictTypeServiceImpl implements DictTypeService {
    @Resource
    private DictTypeMapper dictTypeMapper;
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
}
