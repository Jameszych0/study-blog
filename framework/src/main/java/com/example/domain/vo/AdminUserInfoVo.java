package com.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVo {
    List<String> permissions;
    List<String> roleKeyList;
    UserInfoVo user;
}
