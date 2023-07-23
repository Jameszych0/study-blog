package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
