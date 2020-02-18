package com.ningmeng.manage_course.controller;

import com.ningmeng.api.courseApi.CourseApi;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2020/2/18.
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseApi {
    @Autowired
    private CourseService courseService;

    @GetMapping("/findTeachplanList/{courseId}")
    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @PostMapping("/add")
    @Override
    public ResponseResult add(@RequestBody Teachplan teachplan) {
        return courseService.add(teachplan);
    }
}
