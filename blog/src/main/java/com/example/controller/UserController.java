package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.RegisterDto;
import com.example.domain.dto.UpdateUserInfoDto;
import com.example.domain.entity.User;
import com.example.service.UserService;
import com.example.uitls.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户", description = "用户相关接口")
public class UserController {
    @Resource
    UserService userService;
    @GetMapping("/userInfo")
    @ApiOperation(value = "用户信息", notes = "获取用户信息")
    public ResponseResult<?> userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
    public ResponseResult<?> updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto) {
        User user = BeanCopyUtils.copyBean(updateUserInfoDto, User.class);
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册用户", notes = "注册用户")
    public ResponseResult<?> register(@RequestBody RegisterDto registerDto) {
        User user = BeanCopyUtils.copyBean(registerDto, User.class);
        return userService.register(user);
    }
}
