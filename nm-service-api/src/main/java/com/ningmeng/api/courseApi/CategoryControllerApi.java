package com.ningmeng.api.courseApi;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseMarket;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程分类管理",description = "课程分类管理",tags = {"课程分类管理"})
public interface CategoryControllerApi {
        @ApiOperation("查询分类")
        public CategoryNode findList();
        @ApiOperation("获取课程基础信息")
        public CourseMarket getCourseBaseById(String courseId) throws RuntimeException;
        @ApiOperation("更新课程基础信息")
        public ResponseResult updateCourseBase(String id, CourseBase courseBase);

}