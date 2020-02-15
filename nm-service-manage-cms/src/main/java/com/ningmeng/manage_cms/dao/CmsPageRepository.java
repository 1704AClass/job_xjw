package com.ningmeng.manage_cms.dao;

import com.ningmeng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrator on 2020/2/11.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String>{

            //根据页面名称、站点id、页面访问路径查询
         public   CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
