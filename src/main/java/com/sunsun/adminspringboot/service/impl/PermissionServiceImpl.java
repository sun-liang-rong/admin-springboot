package com.sunsun.adminspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.sunsun.adminspringboot.util.PermissionTreeBuilder;
import com.sunsun.adminspringboot.common.enums.PermissionTypeEnum;
import com.sunsun.adminspringboot.common.exception.BusinessException;
import com.sunsun.adminspringboot.dto.request.PermissionRequest;
import com.sunsun.adminspringboot.dto.response.PermissionListResult;
import com.sunsun.adminspringboot.entity.Permission;
import com.sunsun.adminspringboot.mapper.PermissionMapper;
import com.sunsun.adminspringboot.service.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    public List<PermissionListResult> getPermissionList() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<Permission>();
        wrapper.orderByAsc(Permission::getSortNum);
        // 需要对数据组装 需要返回出去的数据是树形结构
        List<Permission> permissionList = permissionMapper.selectList(wrapper);
        // 对数据实现组装
        List<PermissionListResult> permissionListResult = permissionList.stream()
                .map(PermissionListResult::of)
                .collect(Collectors.toList());
        return PermissionTreeBuilder.buildTree(permissionListResult);
    }

    public Long newPermission(PermissionRequest permissionRequest) {
        validatePermission(permissionRequest);
        Permission permission = buildEntity(permissionRequest);
        permissionMapper.insert(permission);
        // MyBatis-Plus 插入成功后会把自增主键回填到实体，这里返回真实的权限ID
        return permission.getId().longValue();
    }

    @Override
    public Long updatePermission(PermissionRequest permission) {
        if (permission.getId() == null) {
            throw new BusinessException("权限ID不能为空");
        }
        validatePermission(permission);
        Permission permissionEntity = buildEntity(permission);
        int affected = permissionMapper.updateById(permissionEntity);
        if (affected == 0) {
            throw new BusinessException("权限不存在或数据未变化");
        }
        return permission.getId().longValue();
    }

    @Override
    public Long deletePermission(Long id) {
        int affected = permissionMapper.deleteById(id);
        if (affected == 0) {
            throw new BusinessException("权限不存在");
        }
        return id;
    }

    @Override
    public List<String> getAllMenuPaths() {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<Permission>()
                .select(Permission::getPath)
                .eq(Permission::getStatus, 1)
                .in(Permission::getPerType, 1, 2)
                .isNotNull(Permission::getPath)
                .orderByAsc(Permission::getSortNum);
        return permissionMapper.selectList(wrapper).stream()
                .map(Permission::getPath)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 按权限类型校验：类型合法性、类型专属必填字段、父子层级关系
     */
    private void validatePermission(PermissionRequest req) {
        PermissionTypeEnum type = PermissionTypeEnum.of(req.getPerType());
        if (type == null) {
            throw new BusinessException("非法的权限类型：" + req.getPerType());
        }
        // 类型专属必填字段
        switch (type) {
            case DIRECTORY -> requireNotBlank(req.getPath(), "目录的路由地址不能为空");
            case MENU -> {
                requireNotBlank(req.getPath(), "菜单的路由地址不能为空");
                requireNotBlank(req.getComponent(), "菜单的组件路径不能为空");
            }
            case BUTTON -> requireNotBlank(req.getPerKey(), "按钮的权限字符不能为空");
        }
        // 父子层级校验：目录=顶级，菜单的父=目录，按钮的父=菜单
        validateParentType(req, type);
    }

    /**
     * 校验父权限的层级关系
     */
    private void validateParentType(PermissionRequest req, PermissionTypeEnum type) {
        if (req.getParentId() == null) {
            throw new BusinessException("父权限ID不能为空");
        }
        if (type == PermissionTypeEnum.DIRECTORY) {
            if (req.getParentId() != 0) {
                throw new BusinessException("目录必须为顶级节点（父权限ID为0）");
            }
            return;
        }
        if (req.getParentId() == 0) {
            throw new BusinessException("菜单和按钮不能作为顶级节点");
        }
        Permission parent = permissionMapper.selectById(req.getParentId());
        if (parent == null) {
            throw new BusinessException("父权限不存在");
        }
        if (type == PermissionTypeEnum.MENU && parent.getPerType() != PermissionTypeEnum.DIRECTORY.getCode()) {
            throw new BusinessException("菜单的父权限必须是目录");
        }
        if (type == PermissionTypeEnum.BUTTON && parent.getPerType() != PermissionTypeEnum.MENU.getCode()) {
            throw new BusinessException("按钮的父权限必须是菜单");
        }
    }

    /**
     * 按类型构建实体：只设置该类型关注的字段，无关字段保持 null
     */
    private Permission buildEntity(PermissionRequest req) {
        PermissionTypeEnum type = PermissionTypeEnum.of(req.getPerType());
        Permission p = new Permission();
        p.setId(req.getId());
        p.setParentId(req.getParentId());
        p.setPerType(type.getCode());
        p.setName(req.getName());
        p.setSortNum(req.getSortNum() == null ? 0 : req.getSortNum());
        p.setStatus(req.getStatus() == null ? 1 : req.getStatus());
        switch (type) {
            case DIRECTORY -> {
                p.setPath(req.getPath());
                p.setComponent(req.getComponent());   // 可空，前端默认 Layout
                p.setIcon(req.getIcon());
                p.setVisible(req.getVisible() == null ? 1 : req.getVisible());
            }
            case MENU -> {
                p.setPath(req.getPath());
                p.setComponent(req.getComponent());
                p.setIcon(req.getIcon());
                p.setIsCache(req.getIsCache() == null ? 1 : req.getIsCache());
                p.setVisible(req.getVisible() == null ? 1 : req.getVisible());
            }
            case BUTTON -> p.setPerKey(req.getPerKey());
        }
        return p;
    }

    private void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(message);
        }
    }
}
