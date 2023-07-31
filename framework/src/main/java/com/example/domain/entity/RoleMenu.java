package com.example.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author makejava
 * @since 2023-07-31 18:16:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role_menu")
public class RoleMenu {
    //角色ID
    private Long roleId;
    //菜单ID
    private Long menuId;
}
