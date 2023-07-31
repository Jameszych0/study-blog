package com.example.uitls;

import com.example.domain.entity.Menu;
import com.example.domain.vo.TreeSelectVo;

import java.util.List;
import java.util.stream.Collectors;

public class SystemConverter {

    public static List<TreeSelectVo> builderMenuTree(List<Menu> menus) {
        List<TreeSelectVo> treeSelectVos = menus.stream()
                .map(menu -> new TreeSelectVo(menu.getId(), menu.getParentId(), menu.getMenuName(), null    ))
                .collect(Collectors.toList());
        return treeSelectVos.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(m -> m.setChildren(getChildren(m, treeSelectVos)))
                .collect(Collectors.toList());
    }

    public static List<TreeSelectVo> getChildren(TreeSelectVo menu, List<TreeSelectVo> menus) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}
