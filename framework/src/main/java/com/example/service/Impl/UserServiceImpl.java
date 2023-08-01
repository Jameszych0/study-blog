package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Exception.SystemException;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.dto.AddUserDto;
import com.example.domain.dto.ChangeUserStatusDto;
import com.example.domain.dto.ShowUserListDto;
import com.example.domain.dto.UpdateUserDto;
import com.example.domain.entity.Role;
import com.example.domain.entity.User;
import com.example.domain.entity.UserRole;
import com.example.domain.vo.*;
import com.example.enums.AppHttpCodeEnum;
import com.example.mapper.UserMapper;
import com.example.service.RoleService;
import com.example.service.UserRoleService;
import com.example.service.UserService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-07-22 17:10:04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;

    @Override
    public ResponseResult<?> userInfo() {
        // 获取当前用户userId
        Long userId = SecurityUtils.getUserId();
        // 根据userId获取用户信息
        User user = getById(userId);
        // 封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult<?> updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> showUserList(Integer pageNum, Integer pageSize, ShowUserListDto showUserListDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(showUserListDto.getUserName()),
                User::getUserName, showUserListDto.getUserName());
        queryWrapper.eq(StringUtils.hasText(showUserListDto.getPhonenumber()),
                User::getPhonenumber, showUserListDto.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(showUserListDto.getStatus()),
                User::getStatus, showUserListDto.getStatus());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        List<ShowUserListVo> showUserListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ShowUserListVo.class);
        return ResponseResult.okResult(new PageVo(showUserListVos, page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult<?> addUser(AddUserDto addUserDto) {
        //对数据进行非空判断
        if (!StringUtils.hasText(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (emailExist(addUserDto.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (phoneNumberExist(addUserDto.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONE_NOT_NULL);
        }
        addUserDto.setPassword(passwordEncoder.encode(addUserDto.getPassword()));
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);

        List<UserRole> collect = addUserDto.getRoleIds().stream()
                .map(aLong -> new UserRole(user.getId(), aLong))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> delUser(Long id) {
        removeById(id);
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        userRoleService.remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getUserInfo(Long id) {
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, id);
        List<Long> roleIds = userRoleService.list(userRoleLambdaQueryWrapper).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL);
        List<ListAllRoleVo> listAllRoleVos =
                BeanCopyUtils.copyBeanList(roleService.list(roleLambdaQueryWrapper), ListAllRoleVo.class);
        GetUserInfoVo updateUserVo = BeanCopyUtils.copyBean(getById(id), GetUserInfoVo.class);
        return ResponseResult.okResult(new UpdateUserVo(roleIds, listAllRoleVos, updateUserVo));
    }

    @Override
    @Transactional
    public ResponseResult<?> updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, updateUserDto.getId());
        userRoleService.remove(queryWrapper);
        List<UserRole> collect = updateUserDto.getRoleIds().stream()
                .map(aLong -> new UserRole(updateUserDto.getId(), aLong))
                .collect(Collectors.toList());
        userRoleService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> changeStatus(ChangeUserStatusDto changeStatusDto) {
        User user = new User();
        user.setId(changeStatusDto.getUserId());
        user.setStatus(changeStatusDto.getStatus());
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

    private boolean phoneNumberExist(String phoneNumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phoneNumber);
        return count(queryWrapper) > 0;
    }
}

