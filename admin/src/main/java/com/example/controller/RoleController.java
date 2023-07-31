package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.AddRoleDto;
import com.example.domain.dto.ChangeStatusDto;
import com.example.domain.dto.ShowRoleListDto;
import com.example.domain.dto.UpdateRoleDto;
import com.example.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    RoleService roleService;

    @GetMapping("/list")
    public ResponseResult<?> showRoleList(Integer pageNum, Integer pageSize, ShowRoleListDto showRoleListDto) {
        return roleService.showRoleList(pageNum, pageSize, showRoleListDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult<?> changeStatus(@RequestBody ChangeStatusDto changeStatusDto) {
        return roleService.changeStatus(changeStatusDto);
    }

    @PostMapping
    public ResponseResult<?> addRole(@RequestBody AddRoleDto addRoleDto) {
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<?> getInfo(@PathVariable("id") Long id) {
        return roleService.getInfo(id);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> delRole(@PathVariable("id") Long id) {
        return roleService.delRole(id);
    }

    @PutMapping
    public ResponseResult<?> updateRole(@RequestBody UpdateRoleDto updateRoleDto) {
        return roleService.updateRole(updateRoleDto);
    }
}
