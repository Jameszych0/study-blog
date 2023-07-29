package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.dto.TagDto;
import com.example.domain.dto.TagListDto;
import com.example.domain.entity.Tag;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-07-26 17:09:27
 */
public interface TagService extends IService<Tag> {

    ResponseResult<?> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult<?> addTag(TagDto addTagDto);

    ResponseResult<?> delTag(String ids);

    ResponseResult<?> selectTagById(Long id);

    ResponseResult<?> listAllTag();
}
