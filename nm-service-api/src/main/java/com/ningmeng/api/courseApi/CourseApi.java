package com.ningmeng.api.courseApi;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.course.CourseView;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.CourseInfo;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.request.CourseListRequest;
import com.ningmeng.framework.domain.course.request.CoursePublishResult;
import com.ningmeng.framework.domain.portalview.ViewCourse;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Administrator on 2020/2/18.
 */
@Api(value = "课程管理",description = "课程管理，提供页面的增删改查")
public interface CourseApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);


    @ApiOperation("添加课程计划")
    public ResponseResult add(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);


    @ApiOperation("课程视图查询")
    public CourseView courseview(String id);

    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    @ApiOperation("发布课程")
    public CoursePublishResult publish(@PathVariable String id);


}
