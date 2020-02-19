package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.CourseInfo;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.request.CourseListRequest;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;

/**
 * Created by Administrator on 2020/2/18.
 */
public interface CourseService {

    public TeachplanNode findTeachplanList( String courseId);

    public String getTeachplan(String courseid);

    public ResponseResult add(Teachplan teachplan);

    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);
}
