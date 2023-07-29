package com.example.service.Impl;

import com.example.uitls.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    public boolean hasPermission(String permission) {
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        //否则  获取当前登录用户所具有的权限列表 如何判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
