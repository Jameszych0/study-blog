package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Category;
import com.example.domain.vo.ExcelCategoryVo;
import com.example.enums.AppHttpCodeEnum;
import com.example.service.CategoryService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult<?> listAllCategory() {
        return categoryService.listAllCategory();
    }

    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void export(HttpServletResponse response) {
        try {
            // 设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            //获取需要导出的数据
            List<Category> list = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (IOException e) {
            //如果出现异常也要响应json
            ResponseResult<?> result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
