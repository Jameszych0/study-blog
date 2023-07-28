package com.example.controller;

import com.example.Exception.SystemException;
import com.example.domain.ResponseResult;
import com.example.domain.dto.LoginDto;
import com.example.domain.entity.LoginUser;
import com.example.domain.entity.Menu;
import com.example.domain.entity.User;
import com.example.domain.vo.AdminUserInfoVo;
import com.example.domain.vo.RoutersVo;
import com.example.domain.vo.UserInfoVo;
import com.example.enums.AppHttpCodeEnum;
import com.example.service.LoginService;
import com.example.service.MenuService;
import com.example.service.RoleService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LoginController {
    @Resource
    private LoginService loginService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        User user = BeanCopyUtils.copyBean(loginDto, User.class);
        if (!StringUtils.hasText(user.getUserName())) {
            // 提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<?> getInfo() {
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = SecurityUtils.getUserId();
        // 根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(userId);
        // 根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(userId);
        // 获取用户信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        // 封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult<?> getRouters() {
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult<?> logout() {
        return loginService.logout();
    }
}
