package com.ningmeng.api.cmsapi;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.cms.response.CmsPostPageResult;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "CMS页面管理接口",description = "CMS页面管理接口，提供页面的增删改查")
public interface CmsPageControllerApi {

    @ApiOperation(value = "分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",defaultValue = "1",required = true,value = "页数",paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",defaultValue = "5",required = true,value = "每页显示条数",paramType = "path",dataType = "int")
    })
    public QueryResponseResult<CourseBase> findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过ID查询页面")
    public CmsPage findById(String id);
    @ApiOperation("修改页面")
    public CmsPageResult update(String id,CmsPage cmsPage);
    @ApiOperation("通过ID删除页面")
    public ResponseResult delete(String id);
    @ApiOperation("发布页面接口")
    public ResponseResult post(String pageId);
    @ApiOperation("一键发布页面")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);



}
