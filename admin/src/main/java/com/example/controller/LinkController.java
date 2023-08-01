package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.domain.dto.AddLinkDto;
import com.example.domain.dto.ChangeLinkStatusDto;
import com.example.domain.dto.ShowLinkDto;
import com.example.domain.entity.Link;
import com.example.domain.vo.LinkVo;
import com.example.service.LinkService;
import com.example.uitls.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<?> showLinkList(Integer pageNum, Integer pageSize, ShowLinkDto showLinkDto) {
        return linkService.showLinkList(pageNum, pageSize, showLinkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<?> getInfo(@PathVariable("id") Long id) {
        LinkVo linkVo = BeanCopyUtils.copyBean(linkService.getById(id), LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @PutMapping
    public ResponseResult<?> updateLink(@RequestBody LinkVo linkVo) {
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @PostMapping
    public ResponseResult<?> addLink(@RequestBody AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult<?> changeStatus(@RequestBody ChangeLinkStatusDto changeLinkStatusDto) {
        return linkService.changeStatus(changeLinkStatusDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> delLink(@PathVariable("id") Long id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
