package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.entity.Comment;
import com.example.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult<?> commentList(Long articleId,Integer pageNum,Integer pageSize) {
        return commentService.commentList(articleId,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult<?> addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }
}
