package com.sunsun.adminspringboot.controller;


import com.sunsun.adminspringboot.annotation.OperationLog;
import com.sunsun.adminspringboot.common.enums.OperationType;
import com.sunsun.adminspringboot.common.ApiResponse;
import com.sunsun.adminspringboot.dto.request.DictTypePageQuery;
import com.sunsun.adminspringboot.dto.request.DictTypeRequest;
import com.sunsun.adminspringboot.dto.response.PageResult;
import com.sunsun.adminspringboot.entity.DictData;
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

import java.util.List;
import java.util.Map;

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
    @OperationLog(module = "字典管理", operation = "新增字典类型", type = OperationType.INSERT)
    public ApiResponse<Integer> addDictType(@RequestBody @Valid DictTypeRequest dictTypeRequest) {
        return ApiResponse.success(dictTypeService.addDictType(dictTypeRequest));
    }

    @Operation(summary = "修改字典类型", description = "修改字典类型信息（修改时必须传递 id）")
    @SaCheckPermission("system:dict:edit")
    @PostMapping("/updateDictType")
    @OperationLog(module = "字典管理", operation = "修改字典类型", type = OperationType.UPDATE)
    public ApiResponse<Integer> updateDictType(@RequestBody @Valid DictTypeRequest dictTypeRequest) {
        return ApiResponse.success(dictTypeService.updateDictType(dictTypeRequest));
    }

    @Operation(summary = "删除字典类型", description = "根据字典类型 id 删除指定字典类型")
    @SaCheckPermission("system:dict:delete")
    @PostMapping("/deleteDictType/{id}")
    @OperationLog(module = "字典管理", operation = "删除字典类型", type = OperationType.DELETE)
    public ApiResponse<Integer> deleteDictType(@Parameter(description = "字典类型ID", required = true, example = "1") @PathVariable Integer id) {
        return ApiResponse.success(dictTypeService.deleteDictType(id));
    }

    @Operation(summary = "获取所有启用的字典", description = "返回所有启用的字典类型及对应字典数据，结构为 {字典类型编码: [字典数据...]}，登录即可调用，供前端全局加载字典使用")
    @GetMapping("/getAllDict")
    public ApiResponse<Map<String, List<DictData>>> getAllDict() {
        return ApiResponse.success(dictTypeService.getAllDict());
    }

    @Operation(summary = "按类型查询字典数据", description = "根据字典类型编码查询该类型下启用的字典数据列表，登录即可调用")
    @GetMapping("/getDictByType/{dictType}")
    public ApiResponse<List<DictData>> getDictByType(
            @Parameter(description = "字典类型编码", required = true, example = "sys_user_sex") @PathVariable String dictType) {
        return ApiResponse.success(dictTypeService.getDictByType(dictType));
    }
}
