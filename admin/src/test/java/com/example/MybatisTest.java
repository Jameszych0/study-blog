package com.example;

import com.example.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MybatisTest {
    @Resource
    RoleMapper roleMapper;

    @Test
    public void test() {
        System.out.println(roleMapper.selectRoleKeyByUserId(1L));
    }
}
