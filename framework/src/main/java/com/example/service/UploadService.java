package com.example.service;

import com.example.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    ResponseResult<?> uploadImg(MultipartFile img);
}
