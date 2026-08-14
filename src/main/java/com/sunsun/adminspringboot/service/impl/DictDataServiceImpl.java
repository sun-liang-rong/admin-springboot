package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.DictDataPageQuery;
import com.sunsun.adminspringboot.dto.request.DictDataRequest;
import com.sunsun.adminspringboot.entity.DictData;
import com.sunsun.adminspringboot.mapper.DictDataMapper;
import com.sunsun.adminspringboot.service.DictDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DictDataServiceImpl implements DictDataService {

    @Resource
    private DictDataMapper dictDataMapper;
    @Override
    public List<DictData> getDictData(DictDataPageQuery dictDataPageQuery) {
        LambdaQueryWrapper<DictData> lambdaQueryWrapper = new LambdaQueryWrapper<DictData>();
        lambdaQueryWrapper.eq(DictData::getDictType, dictDataPageQuery.getDictType());

        if (StringUtils.hasText(dictDataPageQuery.getDictLabel())) {
            lambdaQueryWrapper.eq(DictData::getDictLabel, dictDataPageQuery.getDictLabel());
        }
        if (dictDataPageQuery.getStatus() != null) {
            lambdaQueryWrapper.eq(DictData::getStatus, dictDataPageQuery.getStatus());
        }
        return dictDataMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public Integer addDictData(DictDataRequest dictDataRequest) {
        try {
            DictData dictData = new DictData();
            dictData.setDictType(dictDataRequest.getDictType());
            dictData.setDictLabel(dictDataRequest.getDictLabel());
            dictData.setDictValue(dictDataRequest.getDictValue());
            dictData.setSort(dictDataRequest.getSort());
            dictData.setStatus(dictDataRequest.getStatus());
            return dictDataMapper.insert(dictData);
        } catch (Exception e) {
            throw new BusinessException("添加字典数据失败");
        }
    }

    @Override
    public Integer updateDictData(DictDataRequest dictDataRequest) {
        try {
            DictData dictData = new DictData();
            dictData.setId(dictDataRequest.getId());
            dictData.setDictType(dictDataRequest.getDictType());
            dictData.setDictLabel(dictDataRequest.getDictLabel());
            dictData.setDictValue(dictDataRequest.getDictValue());
            dictData.setSort(dictDataRequest.getSort());
            dictData.setStatus(dictDataRequest.getStatus());
            return dictDataMapper.updateById(dictData);
        } catch (Exception e) {
            throw new BusinessException("修改字典数据失败");
        }
    }

    @Override
    public Integer deleteDictData(Integer id) {
        try {
            return dictDataMapper.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException("删除字典数据失败");
        }
    }
}
