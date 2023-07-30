package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.AddArticleDto;
import com.example.domain.dto.ContentArticleListDto;
import com.example.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    ArticleService articleService;

    @PostMapping
    public ResponseResult<?> addArticle(@RequestBody AddArticleDto addArticleDto) {
        return articleService.addArticle(addArticleDto);
    }

    @GetMapping("/list")
    public ResponseResult<?> contentArticleList(Integer pageNum,
                                                Integer pageSize,
                                                ContentArticleListDto contentArticleListDto) {
        return articleService.contentArticleList(pageNum, pageSize, contentArticleListDto);
    }
}
