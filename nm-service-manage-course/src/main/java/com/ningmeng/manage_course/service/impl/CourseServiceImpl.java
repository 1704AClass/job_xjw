package com.ningmeng.manage_course.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.cms.response.CmsPostPageResult;
import com.ningmeng.framework.domain.course.*;
import com.ningmeng.framework.domain.course.ext.CourseInfo;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.request.CourseListRequest;
import com.ningmeng.framework.domain.course.request.CoursePublishResult;
import com.ningmeng.framework.domain.course.response.CourseCode;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.dao.*;
import com.ningmeng.manage_course.service.CmsPageClient;
import com.ningmeng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2020/2/18.
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private TeachplanMapper teachplansMapper;
    @Autowired
    private TeachplanDao teachplanDao;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;


    @Override
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplansMapper.findTeachplanList(courseId);
    }

    @Override
    public String getTeachplan(String courseid) {
        Optional<CourseBase> courseBase = courseBaseRepository.findById(courseid);
        if(!courseBase.isPresent())
        {
            return null;
        }
        CourseBase courseBase1 = courseBase.get();
        List<Teachplan> list = teachplanDao.findByParentidAndCourseid("0", courseid);
        if (list==null||list.size()==0)
        {
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseid);
            teachplan.setPname(courseBase1.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplanDao.save(teachplan);
            return teachplan.getId();
        }
        Teachplan teachplan = list.get(0);

        return teachplan.getId();
    }

    @Override
    public ResponseResult add(Teachplan teachplan) {
        if (teachplan!=null|| StringUtils.isEmpty(teachplan.getCourseid())|| StringUtils.isEmpty(teachplan.getPname()))
        {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();

        if (StringUtils.isEmpty(parentid))
        {
            parentid = getTeachplan(courseid);
        }
        Optional<Teachplan> byId = teachplanDao.findById(parentid);
        if(!byId.isPresent())
        {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        Teachplan teachplan1 = byId.get();
        String grade = teachplan1.getGrade();
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");
        if (grade.equals("1"))
        {
            teachplan.setGrade("2");
        }else if (grade.equals("2"))
        {
            teachplan.setGrade("3");
        }
        teachplan.setCourseid(teachplan1.getCourseid());
        teachplanDao.save(teachplan);


        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @Transactional
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest==null)
        {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseListpage = courseMapper.findCourseListpage(courseListRequest);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(courseListpage.getResult());
        queryResult.setTotal(courseListpage.getTotal());

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    //课程视图查询
    public CourseView getCoruseView(String id) {
        CourseView courseView = new CourseView(); //查询课程基本信息
         Optional<CourseBase> optional = courseBaseRepository.findById(id);
         if(optional.isPresent()){
             CourseBase courseBase = optional.get();
             courseView.setCourseBase(courseBase);
         }
         //查询课程营销信息
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
         if(courseMarketOptional.isPresent()){
             CourseMarket courseMarket = courseMarketOptional.get();
             courseView.setCourseMarket(courseMarket); }
             //查询课程图片信息
             Optional<CoursePic> picOptional = coursePicRepository.findById(id);
            if(picOptional.isPresent()){
                CoursePic coursePic = picOptional.get();
                courseView.setCoursePic(picOptional.get());
            }
            //查询课程计划信息
            TeachplanNode teachplanNode = teachplanMapper.findTeachplanList(id);
            courseView.setTeachplanNode(teachplanNode);
            return courseView;
    }
    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if(baseOptional.isPresent()){
        CourseBase courseBase = baseOptional.get();
        return courseBase;
        }
        ExceptionCast.cast( CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        return null; }
        //课程预览
        public CoursePublishResult preview(String courseId){
        CourseBase one = this.findCourseBaseById(courseId);//发布课程预览页面
        CmsPage cmsPage = new CmsPage(); //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点 //模板
        cmsPage.setTemplateId(publish_templateId); //页面名称
        cmsPage.setPageName(courseId+".html"); //页面别名
        cmsPage.setPageAliase(one.getName()); //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath); //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath); //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+courseId); //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.add(cmsPage);
        if(cmsPageResult!=null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        //页面id
         String pageId = cmsPageResult.getCmsPage().getPageId();
        //页面url
         String pageUrl = previewUrl+pageId;
         return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String courseId){
        //课程信息
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult = publish_page(courseId);
        if(cmsPostPageResult!=null){
            ExceptionCast.cast(CommonCode.FAIL);
 }
        //更新课程状态
        CourseBase courseBase = saveCoursePubState(courseId); //课程索引... //课程缓存... //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
        }
        //更新课程发布状态
        private CourseBase saveCoursePubState(String courseId){
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //更新发布状态
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }
    //发布课程正式页面
    public CmsPostPageResult publish_page(String courseId){
            CourseBase one = this.findCourseBaseById(courseId);
            //发布课程预览页面
            CmsPage cmsPage = new CmsPage();
            //站点
            cmsPage.setSiteId(publish_siteId);//课程预览站点 //模板
             cmsPage.setTemplateId(publish_templateId);
             //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);
        //发布页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        return cmsPostPageResult;
        }


}
