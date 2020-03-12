package com.ningmeng.manage_course.service;

import com.ningmeng.framework.client.NmServiceList;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Administrator on 2020/2/23.
 */
@FeignClient(value ="nm-service-manage-cms")
public interface CmsPageClient {
    @GetMapping("/cms/postpage/{pageId}")
    public CmsPage findById(@PathVariable("pageId") String pageId);

    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage);
    //一键发布页面
    @PostMapping("/cms/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}