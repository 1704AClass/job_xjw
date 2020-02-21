package com.ningmeng.manage_course.controller;

import com.ningmeng.api.courseApi.CategoryControllerApi;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseMarket;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2020/2/19.
 */
@RestController
@RequestMapping("/Category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    @Override
    public CategoryNode findList() {
        return categoryService.findList();
    }

    @Override
    public CourseMarket getCourseBaseById(String courseId) throws RuntimeException {
        return categoryService.getCourseBaseById(courseId);
    }

    @Override
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        return categoryService.updateCourseBase(id,courseBase);
    }


}
