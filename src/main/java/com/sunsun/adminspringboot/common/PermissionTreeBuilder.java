package com.sunsun.adminspringboot.common;

import com.sunsun.adminspringboot.dto.response.PermissionListResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限树组装工具：把扁平的权限列表按 parentId 递归组装成树形结构
 */
public final class PermissionTreeBuilder {

    private PermissionTreeBuilder() {
    }

    /**
     * 组装权限树
     *
     * @param list 扁平权限列表（包含所有节点）
     * @return 树形结构（顶层为 parentId=0 的目录节点）
     */
    public static List<PermissionListResult> buildTree(List<PermissionListResult> list) {
        // 顶级节点：parentId 为 0（目录）
        List<PermissionListResult> parentList = list.stream()
                .filter(item -> item.getParentId() != null && item.getParentId().equals(0))
                .collect(Collectors.toList());
        setChildren(parentList, list);
        return parentList;
    }

    /**
     * 递归给每个父节点挂载子节点
     */
    private static void setChildren(List<PermissionListResult> parentList, List<PermissionListResult> allList) {
        for (PermissionListResult parent : parentList) {
            List<PermissionListResult> children = allList.stream()
                    .filter(item -> item.getParentId() != null && item.getParentId().equals(parent.getId()))
                    .collect(Collectors.toList());
            parent.setChildren(children);
            setChildren(children, allList);
        }
    }
}
