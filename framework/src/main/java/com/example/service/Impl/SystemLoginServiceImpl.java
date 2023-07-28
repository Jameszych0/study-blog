package com.example.service.Impl;

import com.example.domain.ResponseResult;
import com.example.domain.entity.LoginUser;
import com.example.domain.entity.User;
import com.example.service.LoginService;
import com.example.uitls.JwtUtil;
import com.example.uitls.RedisCache;
import com.example.uitls.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {
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
        redisCache.setCacheObject("login:" + userId, loginUser);
        // 返回token
        Map<String, String> token = new HashMap<>();
        token.put("token", jwt);
        return ResponseResult.okResult(token);
    }

    @Override
    public ResponseResult<?> logout() {
        redisCache.deleteObject("login:" + SecurityUtils.getUserId());
        return ResponseResult.okResult();
    }
}
