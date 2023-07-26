package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
@Api(tags = "文章分类", description = "文章分类相关接口")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ApiOperation(value = "分类列表", notes = "获取分类列表")
    public ResponseResult<?> getCategoryList() {
        return categoryService.getCategoryList();
    }
}
