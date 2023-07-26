package com.example.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "注册dto")
public class RegisterDto {
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //密码
    private String password;
    //邮箱
    private String email;
}
