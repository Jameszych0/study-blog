package com.example.controller;

import com.example.domain.ResponseResult;
import com.example.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {
    @Resource
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult<?> uploadImg(MultipartFile img) {
        return uploadService.uploadImg(img);
    }
}
