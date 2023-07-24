package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.entity.User;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult<?> userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    public ResponseResult<?> updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody User user) {
        return userService.register(user);
    }
}
