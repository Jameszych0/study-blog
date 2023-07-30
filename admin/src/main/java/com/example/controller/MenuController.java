package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.MenuListDto;
import com.example.domain.entity.Menu;
import com.example.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    MenuService menuService;

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
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
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
}
