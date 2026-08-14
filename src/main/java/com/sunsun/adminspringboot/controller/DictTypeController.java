package com.sunsun.adminspringboot.controller;


import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.DictTypePageQuery;
import com.sunsun.adminspringboot.dto.request.DictTypeRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.DictType;
import com.sunsun.adminspringboot.service.DictTypeService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dict")
@Tag(name = "字典类型管理模块", description = "字典类型的分页查询、新增、修改、删除相关接口")
public class DictTypeController {
    @Resource
    private DictTypeService dictTypeService;

    @Operation(summary = "分页查询字典类型列表", description = "支持按字典类型、字典名称、状态条件精确筛选，返回分页字典类型列表")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/getDictTypeList")
    public ApiResponse<PageResult<DictType>> getDictTypeList(@ParameterObject @Valid DictTypePageQuery dictTypePageQuery) {
        return ApiResponse.success(dictTypeService.getDictTypeList(dictTypePageQuery));
    }

    @Operation(summary = "新增字典类型", description = "创建一个新的字典类型（新增时无需传递 id）")
    @SaCheckPermission("system:dict:add")
    @PostMapping("/addDictType")
    public ApiResponse<Integer> addDictType(@RequestBody @Valid DictTypeRequest dictTypeRequest) {
        return ApiResponse.success(dictTypeService.addDictType(dictTypeRequest));
    }

    @Operation(summary = "修改字典类型", description = "修改字典类型信息（修改时必须传递 id）")
    @SaCheckPermission("system:dict:edit")
    @PostMapping("/updateDictType")
    public ApiResponse<Integer> updateDictType(@RequestBody @Valid DictTypeRequest dictTypeRequest) {
        return ApiResponse.success(dictTypeService.updateDictType(dictTypeRequest));
    }

    @Operation(summary = "删除字典类型", description = "根据字典类型 id 删除指定字典类型")
    @SaCheckPermission("system:dict:delete")
    @PostMapping("/deleteDictType/{id}")
    public ApiResponse<Integer> deleteDictType(@Parameter(description = "字典类型ID", required = true, example = "1") @PathVariable Integer id) {
        return ApiResponse.success(dictTypeService.deleteDictType(id));
    }
}
