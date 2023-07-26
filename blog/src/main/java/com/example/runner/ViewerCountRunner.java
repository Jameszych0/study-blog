package com.example.runner;

import com.example.domain.entity.Article;
import com.example.mapper.ArticleMapper;
import com.example.uitls.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewerCountRunner implements CommandLineRunner {
    @Resource
    ArticleMapper articleMapper;
    @Resource
    RedisCache redisCache;

    @Override
    public void run(String... args) {
        // 查询博客信息  id  viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString()
                        , article -> article.getViewCount().intValue()));
        redisCache.setCacheMap("article:viewCount", viewCountMap);
    }
}
