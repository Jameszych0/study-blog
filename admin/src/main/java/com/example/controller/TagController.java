package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.TagDto;
import com.example.domain.entity.Tag;
import com.example.domain.dto.TagListDto;
import com.example.service.TagService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<?> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult<?> listAllTag() {
        return tagService.listAllTag();
    }

    @PostMapping
    public ResponseResult<?> addTag(@RequestBody TagDto addTagDto) {
        return tagService.addTag(addTagDto);
    }

    @DeleteMapping("/{ids}")
    public ResponseResult<?> delTag(@PathVariable("ids") String ids) {
        return tagService.delTag(ids);
    }

    @GetMapping("/{id}")
    public ResponseResult<?> selectTagById(@PathVariable("id") Long id) {
        return tagService.selectTagById(id);
    }

    @PutMapping
    public ResponseResult<?> updateTag(@RequestBody TagDto updateTagDto) {
        Tag tag = BeanCopyUtils.copyBean(updateTagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }
}
