package com.example.service.Impl;

import com.example.domain.ResponseResult;
import com.example.domain.entity.LoginUser;
import com.example.domain.entity.User;
import com.example.domain.vo.BlogUserLoginVo;
import com.example.domain.vo.UserInfoVo;
import com.example.service.BlogLoginService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.JwtUtil;
import com.example.uitls.RedisCache;
import com.example.uitls.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult<?> login(User user) {
        // 获取授权信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断用户是否存在
        if (Objects.isNull(authenticate))
            throw new RuntimeException("用户名或密码错误");
        // 通过userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);
        // 把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        // 把token和userinfo封装 返回
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult<?> logout() {
        // 获取useId TODO 封装成工具类
        String userId = SecurityUtils.getUserId().toString();
        // 删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
