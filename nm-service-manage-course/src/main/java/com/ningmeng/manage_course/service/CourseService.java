package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.model.response.ResponseResult;

/**
 * Created by Administrator on 2020/2/18.
 */
public interface CourseService {

    public TeachplanNode findTeachplanList( String courseId);

    public String getTeachplan(String courseid);

    public ResponseResult add(Teachplan teachplan);
}
