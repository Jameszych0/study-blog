package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.domain.ResponseResult;
import com.example.domain.dto.MenuListDto;
import com.example.domain.entity.Menu;
import com.example.domain.entity.RoleMenu;
import com.example.domain.vo.RoleMenuTreeSelectVo;
import com.example.domain.vo.TreeSelectVo;
import com.example.service.MenuService;
import com.example.service.RoleMenuService;
import com.example.uitls.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    MenuService menuService;
    @Resource
    RoleMenuService roleMenuService;

    @GetMapping("/list")
    public ResponseResult<?> menuList(MenuListDto menuListDto) {
        return menuService.menuList(menuListDto);
    }

    @PostMapping
    public ResponseResult<?> addMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{menuId}")
    public ResponseResult<?> getInfo(@PathVariable("menuId") Long menuId) {
        return ResponseResult.okResult(menuService.getById(menuId));
    }

    @PutMapping
    public ResponseResult<?> edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> delMenu(@PathVariable("id") Long id) {
        if (menuService.hasChild(id))
            return ResponseResult.errorResult(500, "存在子菜单，不允许删除");
        menuService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/treeselect")
    public ResponseResult<?> treeSelect() {
        return menuService.treeSelect();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult<?> roleMenuTreeSelectById(@PathVariable("id") Long id) {
        List<TreeSelectVo> treeSelectVos = SystemConverter.builderMenuTree(menuService.list());
        List<Long> checkedKeys = roleMenuService.checkedKeys(id);
        RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(treeSelectVos, checkedKeys);
        return ResponseResult.okResult(roleMenuTreeSelectVo);
    }
}
