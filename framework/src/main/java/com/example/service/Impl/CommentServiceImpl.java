package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Comment;
import com.example.domain.vo.CommentVo;
import com.example.domain.vo.PageVo;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.uitls.BeanCopyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-07-22 13:56:32
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    UserService userService;

    @Override
    public ResponseResult<?> commentList(Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 根据articleId查询文章的评论
        queryWrapper.eq(Comment::getArticleId, articleId);
        // 根据rootId为1来判断根评论
        queryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // vo优化
        List<CommentVo> commentVos = toCommentVoList(page.getRecords());
        // 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        commentVos = commentVos.stream()
                .peek(commentVo -> commentVo.setChildren(getChildren(commentVo.getId()))).collect(Collectors.toList());
        return ResponseResult.okResult(new PageVo(commentVos, page.getTotal()));
    }

    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        return toCommentVoList(comments);
    }

    public List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        return commentVos.stream()
                .peek(commentVo -> commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName()))
                .peek(commentVo -> {
                    if (commentVo.getToCommentUserId() != -1)
                        commentVo.setToCommentUserName
                                (userService.getById(commentVo.getToCommentUserId()).getNickName());
                })
                .collect(Collectors.toList());
    }
}

