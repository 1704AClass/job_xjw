package com.ningmeng.manage_course.controller;

import com.ningmeng.api.courseApi.CourseApi;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseView;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.CourseInfo;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.request.CourseListRequest;
import com.ningmeng.framework.domain.course.request.CoursePublishResult;
import com.ningmeng.framework.domain.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.service.CmsPageClient;
import com.ningmeng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2020/2/18.
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseApi {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CmsPageClient cmsPageClient;

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

    @GetMapping("/findCourseList/page/size")
    @PreAuthorize("hasAuthority('course_find_list')")
    @Override
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, @RequestBody CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
    }

    @Override
    @GetMapping("/courseview/${id}")
    public CourseView courseview(@PathVariable String id) {
        return courseService.getCoruseView(id);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageClient.add( cmsPage );
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview( id );
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {
        return courseService.publish( id );
    }

    @PreAuthorize("hasAuthority('course_get_baseinfo')")
    @GetMapping("/getCourseBaseById/${courseId}")
    @Override
    public CourseBase findCourseBaseById(@PathVariable("courseId")String courseId) {
        return courseService.findCourseBaseById( courseId );
    }
}
