package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-07-22 13:56:32
 */
public interface CommentService extends IService<Comment> {

    ResponseResult<?> commentList(Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult<?> addComment(Comment comment);
}
