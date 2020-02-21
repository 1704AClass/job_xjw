package com.ningmeng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.config.RabbitmqConfig;
import com.ningmeng.manage_cms.dao.CmsPageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ResponseResult postpage(String pageId)
    {
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage==null)
        {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String siteId = cmsPage.getSiteId();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageId",pageId);
        String msg = JSON.toJSONString(map);
        boolean flag=createHtml();
        if(!flag)
        {
            ExceptionCast.cast(CommonCode.FAIL);

        }
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,msg);
        return  new ResponseResult(CommonCode.SUCCESS);
    }
    private boolean createHtml()
    {
        System.out.println("保存静态页面完成");
        return true;
    }
    public QueryResponseResult<CourseBase> findList(int page, int size, QueryPageRequest queryPageRequest){
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        if(page<=0){
            page = 1;
        }
        page = page-1;
        PageRequest pageRequest = PageRequest.of(page,size);
        //构建条件构建器
        CmsPage cmsPage = new CmsPage();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        if(queryPageRequest.getPageAliase() !=null){
            exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(queryPageRequest.getSiteId() !=null){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(queryPageRequest.getTemplateId() !=null){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //构造条件
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        Page<CmsPage> listAll = cmsPageRepository.findAll(pageRequest);
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<>();
        cmsPageQueryResult.setList(listAll.getContent());
        cmsPageQueryResult.setTotal(listAll.getTotalElements());
        return new QueryResponseResult<CourseBase>(CommonCode.SUCCESS,cmsPageQueryResult);
    }

    //添加页面
    public CmsPageResult add(CmsPage cmsPage){
        //校验页面是否存在，根据页面名称、站点Id、页面webpath查询        
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
        if(cmsPage1==null){
            cmsPage.setPageId(null);//添加页面主键由spring data 自动生成
            cmsPageRepository.save(cmsPage);
            //返回结果
            CmsPageResult   cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            return  cmsPageResult;
            }
            return new CmsPageResult(CommonCode.FAIL,null);
    }
    //根据id查询页面
    public CmsPage findById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
            //返回空
            return null;
    }
        //更新页面信息
        public CmsPageResult update(String id,CmsPage cmsPage) {
            //根据id查询页面信息
            CmsPage one = this.findById(id);
            if (one != null) {
                //更新模板
                one.setTemplateId(cmsPage.getTemplateId());
                //更新所属站点
                one.setSiteId(cmsPage.getSiteId());
                //更新页面别名
                one.setPageAliase(cmsPage.getPageAliase());
                //更新页面名称
                one.setPageName(cmsPage.getPageName());
                //更新访问路径
                one.setPageWebPath(cmsPage.getPageWebPath());
                //更新物理路径
                one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
                //执行更新
                CmsPage save = cmsPageRepository.save(one);
                if (save != null){
                    //返回成功
                    CmsPageResult cmsPageResult =new CmsPageResult(CommonCode.SUCCESS,save);
                    return cmsPageResult;
                        }
                }
                //返回失败
                return new CmsPageResult(CommonCode.FAIL,null);
        }
        //删除页面
        public  ResponseResult delete(String id){
            CmsPage one = this.findById(id);
            if(one!=null){
                //删除页面
                cmsPageRepository.deleteById(id);
                return new ResponseResult(CommonCode.SUCCESS);
            }
            return new ResponseResult(CommonCode.FAIL);
        }
}
