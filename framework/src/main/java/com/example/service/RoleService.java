package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-07-27 15:29:22
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}
