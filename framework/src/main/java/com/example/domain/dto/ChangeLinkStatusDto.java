package com.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLinkStatusDto {
    private Long id;
    //角色状态（0正常 1停用）
    private String status;
}
