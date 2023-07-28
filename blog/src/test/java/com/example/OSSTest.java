package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class OSSTest {
    @Test
    public void testOss() {
        String ids = "1";
        String[] idArray = ids.split(","); // 按照逗号分割成字符串数组

        int[] intArray = new int[idArray.length]; // 创建整数数组

        for (int i = 0; i < idArray.length; i++) {
            intArray[i] = Integer.parseInt(idArray[i]); // 将每个字符串转换为整数
        }

        System.out.println(Arrays.toString(intArray)); // 打印整数数组
    }
}
