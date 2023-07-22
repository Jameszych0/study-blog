package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
