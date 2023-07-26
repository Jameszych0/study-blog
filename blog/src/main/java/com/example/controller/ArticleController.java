package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
@Api(tags = "文章", description = "文章相关接口")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热门文章列表", notes = "获取阅读量前十的文章放在右侧")
    public ResponseResult<?> hotArticleList() {
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @ApiOperation(value = "文章列表", notes = "获取文章大纲展示在首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小"),
            @ApiImplicitParam(name = "categoryId", value = "分类Id")
    })
    public ResponseResult<?> articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "文章详情", notes = "展示文章详情")
    @ApiImplicitParam(name = "id", value = "文章Id")
    public ResponseResult<?> getArticleDetail(@PathVariable("id") Long id) {
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "更新阅读量", notes = "更新文章阅读量")
    @ApiImplicitParam(name = "id", value = "文章Id")
    public ResponseResult<?> updateViewCount(@PathVariable("id") Long id) {
        return articleService.updateViewCount(id);
    }
}
