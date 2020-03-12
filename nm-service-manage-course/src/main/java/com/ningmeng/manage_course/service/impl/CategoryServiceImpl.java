package com.ningmeng.manage_course.service.impl;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseMarket;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.dao.CategoryMapper;
import com.ningmeng.manage_course.dao.CourseBaseDao;
import com.ningmeng.manage_course.dao.CourseMarketRepository;
import com.ningmeng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Administrator on 2020/2/19.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CourseBaseDao courseBaseDao;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Override
    public CategoryNode findList() {
        return categoryMapper.findLit();
    }

    @Override
    public CourseMarket getCourseBaseById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(!optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Override
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        courseBase.setId(id);
        courseBaseDao.saveAndFlush(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
