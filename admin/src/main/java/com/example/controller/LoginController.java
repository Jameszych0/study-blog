package com.example.controller;

import com.example.Exception.SystemException;
import com.example.domain.ResponseResult;
import com.example.domain.dto.LoginDto;
import com.example.domain.entity.User;
import com.example.enums.AppHttpCodeEnum;
import com.example.service.LoginService;
import com.example.uitls.BeanCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        User user = BeanCopyUtils.copyBean(loginDto, User.class);
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
}
