package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@Api(tags = "上传", description = "上传相关接口")
public class UploadController {
    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传", notes = "上传头像")
    public ResponseResult<?> uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }
}
