package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.ResponseResult;
import com.example.domain.dto.TagDto;
import com.example.domain.dto.TagListDto;
import com.example.domain.entity.Tag;
import com.example.domain.vo.PageVo;
import com.example.domain.vo.TagVo;
import com.example.mapper.TagMapper;
import com.example.service.TagService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-07-26 17:09:27
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Resource
    TagMapper tagMapper;

    @Override
    public ResponseResult<?> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);
        return ResponseResult.okResult(new PageVo(tagVos, page.getTotal()));
    }

    @Override
    public ResponseResult<?> addTag(TagDto addTagDto) {
        Tag tag = new Tag();
        tag.setCreateBy(SecurityUtils.getUserId());
        tag.setName(addTagDto.getName());
        tag.setRemark(addTagDto.getRemark());
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> delTag(String ids) {
        String[] idArray = ids.split(",");
        List<Long> collect = Arrays.stream(idArray)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        tagMapper.deleteBatchIds(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> selectTagById(Long id) {
        TagVo tagVo = BeanCopyUtils.copyBean(getById(id), TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

}

