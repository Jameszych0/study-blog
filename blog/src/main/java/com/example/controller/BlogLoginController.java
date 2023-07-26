package com.example.controller;

import com.example.Exception.SystemException;
import com.example.domain.ResponseResult;
import com.example.domain.dto.LoginDto;
import com.example.domain.entity.User;
import com.example.enums.AppHttpCodeEnum;
import com.example.service.BlogLoginService;
import com.example.uitls.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "登录", description = "登录相关接口")
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        User user = BeanCopyUtils.copyBean(loginDto, User.class);
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出", notes = "退出登录")
    public ResponseResult<?> logout() {
        return blogLoginService.logout();
    }
}
