package com.example.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "更新用户信息dto")
public class UpdateUserInfoDto {
    //主键
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //邮箱
    private String email;
    @ApiModelProperty(notes = "用户性别（0男，1女，2未知）")
    private String sex;
    //头像
    private String avatar;
}
