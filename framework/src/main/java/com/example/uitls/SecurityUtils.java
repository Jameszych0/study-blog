package com.example.uitls;

import com.example.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取userId
     */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }

    public static Boolean isAdmin() {
        Long id = getUserId();
        return id != null && id.equals(1L);
    }
}
