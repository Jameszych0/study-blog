package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.dto.ChangeLinkStatusDto;
import com.example.domain.dto.ShowLinkDto;
import com.example.domain.entity.Link;
import com.example.domain.vo.LinkVo;
import com.example.domain.vo.PageVo;
import com.example.mapper.LinkMapper;
import com.example.service.LinkService;
import com.example.uitls.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-07-20 16:18:01
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult<?> getAllLink() {
        // 通过status查询友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        // 转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<?> showLinkList(Integer pageNum, Integer pageSize, ShowLinkDto showLinkDto) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(showLinkDto.getName()), Link::getName, showLinkDto.getName());
        queryWrapper.like(StringUtils.hasText(showLinkDto.getStatus()), Link::getStatus, showLinkDto.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos, page.getTotal()));
    }

    @Override
    public ResponseResult<?> changeStatus(ChangeLinkStatusDto changeLinkStatusDto) {
        Link link = new Link();
        link.setId(changeLinkStatusDto.getId());
        link.setStatus(changeLinkStatusDto.getStatus());
        updateById(link);
        return ResponseResult.okResult();
    }
}

