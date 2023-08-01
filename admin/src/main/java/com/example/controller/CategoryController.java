package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.domain.ResponseResult;
import com.example.domain.dto.AddCategoryDto;
import com.example.domain.dto.ShowCategoryListDto;
import com.example.domain.entity.Category;
import com.example.domain.vo.ExcelCategoryVo;
import com.example.domain.vo.GetCategoryInfoVo;
import com.example.enums.AppHttpCodeEnum;
import com.example.service.CategoryService;
import com.example.uitls.BeanCopyUtils;
import com.example.uitls.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    public ResponseResult<?> showCategoryList(Integer pageNum,
                                              Integer pageSize,
                                              ShowCategoryListDto showCategoryListDto) {
        return categoryService.showCategoryList(pageNum, pageSize, showCategoryListDto);
    }

    @PostMapping
    public ResponseResult<?> addCategory(@RequestBody AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult<?> getInfo(@PathVariable("id") Long id) {
        GetCategoryInfoVo getCategoryInfoVo =
                BeanCopyUtils.copyBean(categoryService.getById(id), GetCategoryInfoVo.class);
        return ResponseResult.okResult(getCategoryInfoVo);
    }

    @PutMapping
    public ResponseResult<?> updateCategory(@RequestBody GetCategoryInfoVo categoryInfoVo) {
        Category category = BeanCopyUtils.copyBean(categoryInfoVo, Category.class);
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult<?> delCategory(@PathVariable("id") Long id) {
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

}
