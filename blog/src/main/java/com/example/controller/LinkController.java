package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.LinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/link")
@Api(tags = "友链", description = "友链相关接口")
public class LinkController {

    @Resource
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @ApiOperation(value = "友链列表", notes = "获取友链列表")
    public ResponseResult<?> getAllLink() {
        return linkService.getAllLink();
    }
}
