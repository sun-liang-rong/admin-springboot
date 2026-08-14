package com.sunsun.adminspringboot.controller;

import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.DictDataPageQuery;
import com.sunsun.adminspringboot.dto.request.DictDataRequest;
import com.sunsun.adminspringboot.entity.DictData;
import com.sunsun.adminspringboot.service.DictDataService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dictData")
@Tag(name = "字典数据管理模块", description = "字典数据（字典值）的查询、新增、修改、删除相关接口")
public class DictDataController {

    @Resource
    private DictDataService dictDataService;

    @Operation(summary = "查询字典数据列表", description = "按字典类型编码查询字典数据（dictType 必传），支持按字典标签、状态条件精确筛选，返回字典数据列表")
    @SaCheckPermission("system:dictData:list")
    @GetMapping("/getDictData")
    public ApiResponse<List<DictData>> getDictData(@ParameterObject @Valid DictDataPageQuery dictDataPageQuery) {
        return ApiResponse.success(dictDataService.getDictData(dictDataPageQuery));
    }

    @Operation(summary = "新增字典数据", description = "创建一个新的字典数据（新增时无需传递 id）")
    @SaCheckPermission("system:dictData:add")
    @PostMapping("/addDictData")
    public ApiResponse<Integer> addDictData(@RequestBody @Valid DictDataRequest dictDataRequest) {
        return ApiResponse.success(dictDataService.addDictData(dictDataRequest));
    }

    @Operation(summary = "修改字典数据", description = "修改字典数据信息（修改时必须传递 id）")
    @SaCheckPermission("system:dictData:edit")
    @PostMapping("/updateDictData")
    public ApiResponse<Integer> updateDictData(@RequestBody @Valid DictDataRequest dictDataRequest) {
        return ApiResponse.success(dictDataService.updateDictData(dictDataRequest));
    }

    @Operation(summary = "删除字典数据", description = "根据字典数据 id 删除指定字典数据")
    @SaCheckPermission("system:dictData:delete")
    @PostMapping("/deleteDictData/{id}")
    public ApiResponse<Integer> deleteDictData(@Parameter(description = "字典数据ID", required = true, example = "1") @PathVariable Integer id) {
        return ApiResponse.success(dictDataService.deleteDictData(id));
    }
}
