package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.entity.Menu;
import com.example.domain.entity.RoleMenu;
import com.example.mapper.RoleMenuMapper;
import com.example.service.MenuService;
import com.example.service.RoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-07-31 18:27:27
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    @Resource
    MenuService menuService;

    @Override
    public List<Long> checkedKeys(Long id) {
        if (id.equals(1L)) {
            return menuService.list().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList());
        }
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        return list(queryWrapper).stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }
}

