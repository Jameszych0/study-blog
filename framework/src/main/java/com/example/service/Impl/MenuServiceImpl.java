package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.dto.MenuListDto;
import com.example.domain.entity.Menu;
import com.example.domain.vo.MenuVo;
import com.example.mapper.MenuMapper;
import com.example.service.MenuService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-07-27 15:28:59
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        // 如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(queryWrapper);
            return list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }
        // 否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(userId);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus;
        // 判断是否为管理员
        if (SecurityUtils.isAdmin())
            menus = getBaseMapper().selectAllRouterMenu();
        else
            menus = getBaseMapper().selectRouterMenuTreeByUserId(userId);
        // 设置子属性
        return builderMenuTree(menus);
    }

    @Override
    public ResponseResult<?> menuList(MenuListDto menuListDto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getOrderNum, Menu::getId);
        queryWrapper.like(StringUtils.hasText(menuListDto.getMenuName()) ,Menu::getMenuName ,menuListDto.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menuListDto.getStatus()) ,Menu::getStatus, menuListDto.getStatus());
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list(queryWrapper), MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public boolean hasChild(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        return count(queryWrapper) != 0;
    }

    private List<Menu> builderMenuTree(List<Menu> menus) {
        return menus.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
    }

    // 获取存入参数的子Menu集合
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}

