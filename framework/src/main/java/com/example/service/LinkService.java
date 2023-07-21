package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-07-20 16:18:00
 */
public interface LinkService extends IService<Link> {

    ResponseResult<?> getAllLink();
}
