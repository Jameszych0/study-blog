package com.example.job;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.domain.entity.Article;
import com.example.service.ArticleService;
import com.example.uitls.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Resource
    RedisCache redisCache;
    @Resource
    ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateViewCount() {
        //获取redis中的浏览
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articles = cacheMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articles.forEach(
                e -> {
                    UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("id", e.getId());
                    updateWrapper.set("view_count", e.getViewCount());
                    articleService.update(updateWrapper);
                }
        );
    }
}
