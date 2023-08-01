package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.dto.AddUserDto;
import com.example.domain.dto.ChangeUserStatusDto;
import com.example.domain.dto.ShowUserListDto;
import com.example.domain.dto.UpdateUserDto;
import com.example.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-07-22 17:10:02
 */
public interface UserService extends IService<User> {

    ResponseResult<?> userInfo();

    ResponseResult<?> updateUserInfo(User user);

    ResponseResult<?> register(User user);

    ResponseResult<?> showUserList(Integer pageNum, Integer pageSize, ShowUserListDto showUserListDto);

    ResponseResult<?> addUser(AddUserDto addUserDto);

    ResponseResult<?> delUser(Long id);

    ResponseResult<?> getUserInfo(Long id);

    ResponseResult<?> updateUser(UpdateUserDto updateUserDto);

    ResponseResult<?> changeStatus(ChangeUserStatusDto changeStatusDto);
}
