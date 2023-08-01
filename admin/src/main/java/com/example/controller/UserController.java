package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.*;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/list")
    public ResponseResult<?> showUserList(Integer pageNum, Integer pageSize, ShowUserListDto showUserListDto) {
        return userService.showUserList(pageNum, pageSize, showUserListDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult<?> changeStatus(@RequestBody ChangeUserStatusDto changeStatusDto) {
        return userService.changeStatus(changeStatusDto);
    }

    @PostMapping
    public ResponseResult<?> addUser(@RequestBody AddUserDto addUserDto) {
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> delUser(@PathVariable("id") Long id) {
        return userService.delUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult<?> getUserInfo(@PathVariable("id") Long id) {
        return userService.getUserInfo(id);
    }

    @PutMapping
    public ResponseResult<?> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(updateUserDto);
    }
}
