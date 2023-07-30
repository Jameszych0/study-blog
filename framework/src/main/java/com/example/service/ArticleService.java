package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.dto.AddArticleDto;
import com.example.domain.dto.ContentArticleListDto;
import com.example.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult<?> hotArticleList();

    ResponseResult<?> articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult<?> getArticleDetail(Long id);

    ResponseResult<?> updateViewCount(Long id);

    ResponseResult<?> addArticle(AddArticleDto addArticleDto);

    ResponseResult<?> contentArticleList(Integer pageNum, Integer pageSize, ContentArticleListDto contentArticleListDto);
}