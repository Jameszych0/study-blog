package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2023-07-31 18:27:26
 */
public interface RoleMenuService extends IService<RoleMenu> {

    List<Long> checkedKeys(Long id);
}
