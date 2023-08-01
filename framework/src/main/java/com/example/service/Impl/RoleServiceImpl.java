package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.dto.AddRoleDto;
import com.example.domain.dto.ChangeStatusDto;
import com.example.domain.dto.ShowRoleListDto;
import com.example.domain.dto.UpdateRoleDto;
import com.example.domain.entity.Role;
import com.example.domain.entity.RoleMenu;
import com.example.domain.vo.ListAllRoleVo;
import com.example.domain.vo.PageVo;
import com.example.domain.vo.RoleVo;
import com.example.mapper.RoleMapper;
import com.example.service.RoleMenuService;
import com.example.service.RoleService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-07-27 15:29:22
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        // 判断是否是管理员 如果是返回集合中只需要有admin
        if (SecurityUtils.isAdmin()) {
            List<String> roleKey = new ArrayList<>();
            roleKey.add("admin");
            return roleKey;
        }
        // 否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(userId);
    }

    @Override
    public ResponseResult<?> showRoleList(Integer pageNum, Integer pageSize, ShowRoleListDto showRoleListDto) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(showRoleListDto.getRoleName()),
                Role::getRoleName, showRoleListDto.getRoleName());
        queryWrapper.eq(StringUtils.hasText(showRoleListDto.getStatus()),
                Role::getStatus, showRoleListDto.getStatus());
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        PageVo pageVo = new PageVo(roleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> changeStatus(ChangeStatusDto changeStatusDto) {
        Role role = new Role();
        role.setId(changeStatusDto.getRoleId());
        role.setStatus(changeStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        List<RoleMenu> roleMenus = addRoleDto.getMenuIds().stream()
                .map(aLong -> new RoleMenu(role.getId(), aLong))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getInfo(Long id) {
        RoleVo roleVo = BeanCopyUtils.copyBean(getById(id), RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> updateRole(UpdateRoleDto updateRoleDto) {
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, updateRoleDto.getId());
        roleMenuService.remove(queryWrapper);

        List<RoleMenu> collect = updateRoleDto.getMenuIds().stream()
                .map(aLong -> new RoleMenu(updateRoleDto.getId(), aLong))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(collect);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> delRole(Long id) {
        removeById(id);

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        roleMenuService.remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL);
        List<ListAllRoleVo> listAllRoleVos = BeanCopyUtils.copyBeanList(list(queryWrapper), ListAllRoleVo.class);
        return ResponseResult.okResult(listAllRoleVos);
    }
}