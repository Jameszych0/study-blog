package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult<?> hotArticleList();

    ResponseResult<?> articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult<?> getArticleDetail(Long id);

    ResponseResult<?> updateViewCount(Long id);
}