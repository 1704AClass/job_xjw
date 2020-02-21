package com.ningmeng.manage_cms.controller;

import com.ningmeng.api.cmsapi.CmsPageControllerApi;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CourseBase> findList(@PathVariable("page") int page, @PathVariable("size") int size, @RequestBody QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(page,size,queryPageRequest);
    }

    //添加页面
    @Override
    @PostMapping("/add")
    public  CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }
    //回显
    @Override
    @GetMapping("/findone/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.findById(id);
    }
    //修改
    @Override
    @PutMapping("/update/{id}")
    public CmsPageResult update(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id,cmsPage);
    }
    //删除
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }

    @Override
    @GetMapping("postpage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return cmsPageService.postpage(pageId);
    }


}
