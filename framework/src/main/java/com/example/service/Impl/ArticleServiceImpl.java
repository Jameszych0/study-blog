package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Article;
import com.example.domain.vo.ArticleDetailVo;
import com.example.domain.vo.ArticleListVo;
import com.example.domain.vo.HotArticleVo;
import com.example.domain.vo.PageVo;
import com.example.mapper.ArticleMapper;
import com.example.service.ArticleService;
import com.example.service.CategoryService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.RedisCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    CategoryService categoryService;
    @Resource
    RedisCache redisCache;

    @Override
    public ResponseResult<?> hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        //通过redis查询ViewCount
        List<Article> collect = articles.stream()
                .peek(article -> {
                    Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
                    article.setViewCount(viewCount.longValue());
                })
                .collect(Collectors.toList());
        //bean拷贝
        List<HotArticleVo> vo = BeanCopyUtils.copyBeanList(collect, HotArticleVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult<?> articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果categoryId不为空，添加categoryId查询条件
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        // 只能查询正式发布的文章，按置顶文章排序
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getIsTop);

        // 分类页面
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .peek(article -> {
                    Integer viewCount = redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
                    article.setViewCount(viewCount.longValue());
                })
                .collect(Collectors.toList());

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        // 转换vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据分类id查询分类名
        articleDetailVo.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
        // 封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult<?> updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
}
}