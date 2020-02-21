package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseMarket;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.model.response.ResponseResult;

/**
 * Created by Administrator on 2020/2/19.
 */
public interface CategoryService {
    CategoryNode findList();

    CourseMarket getCourseBaseById(String courseId);

    ResponseResult updateCourseBase(String id, CourseBase courseBase);
}
