package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult<?> listAllCategory() {
        return categoryService.listAllCategory();
    }
}
