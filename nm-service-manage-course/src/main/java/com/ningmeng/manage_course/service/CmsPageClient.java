package com.ningmeng.manage_course.service;

import com.ningmeng.framework.client.NmServiceList;
import com.ningmeng.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Administrator on 2020/2/23.
 */
@FeignClient(value ="nm-service-manage-cms")
public interface CmsPageClient {
    @GetMapping("/cms/postpage/{pageId}")
    public CmsPage findById(@PathVariable("pageId") String pageId);
}