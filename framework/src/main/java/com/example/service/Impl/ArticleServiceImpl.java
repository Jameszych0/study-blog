package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.dto.AddArticleDto;
import com.example.domain.dto.ContentArticleListDto;
import com.example.domain.dto.UpdateArticleDto;
import com.example.domain.entity.Article;
import com.example.domain.entity.ArticleTag;
import com.example.domain.vo.*;
import com.example.mapper.ArticleMapper;
import com.example.service.ArticleService;
import com.example.service.ArticleTagService;
import com.example.service.CategoryService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    CategoryService categoryService;
    @Resource
    ArticleTagService articleTagService;
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
                    Integer viewCount =
                            redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
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
                    Integer viewCount =
                            redisCache.getCacheMapValue("article:viewCount", article.getId().toString());
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
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> addArticle(AddArticleDto addArticleDto) {
        // 添加文章
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);
        save(article);
        // 添加文章与标签关联
        List<ArticleTag> collect = addArticleDto.getTags().stream()
                .map(aLong -> new ArticleTag(article.getId(), aLong))
                .collect(Collectors.toList());
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> contentArticleList(Integer pageNum, Integer pageSize,
                                                ContentArticleListDto contentArticleListDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(contentArticleListDto.getTitle()),
                Article::getTitle, contentArticleListDto.getTitle());
        queryWrapper.like(StringUtils.hasText(contentArticleListDto.getSummary()),
                Article::getSummary, contentArticleListDto.getSummary());
        // 分类页面
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<ContentArticleListVo> contentArticleListVos =
                BeanCopyUtils.copyBeanList(page.getRecords(), ContentArticleListVo.class);
        PageVo pageVo = new PageVo(contentArticleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> showArticle(Long id) {
        ShowArticleVo showArticleVo = BeanCopyUtils.copyBean(getById(id), ShowArticleVo.class);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<Long> tags = articleTagService.list(queryWrapper).stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        showArticleVo.setTags(tags);
        return ResponseResult.okResult(showArticleVo);
    }

    @Override
    @Transactional
    public ResponseResult<?> updateArticle(UpdateArticleDto updateArticleDto) {
        // 添加文章
        Article article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        updateById(article);
        // 添加文章与标签关联
        List<ArticleTag> collect = updateArticleDto.getTags().stream()
                .map(aLong -> new ArticleTag(article.getId(), aLong))
                .collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(queryWrapper);
        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult<?> delArticle(String ids) {
        String[] idArray = ids.split(",");
        List<Long> collect = Arrays.stream(idArray)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ArticleTag::getArticleId, collect);
        articleTagService.remove(queryWrapper);
        removeBatchByIds(collect);
        return ResponseResult.okResult();
    }
}