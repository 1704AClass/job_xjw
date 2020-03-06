package com.ningmeng.manage_media.service;

import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.TeachplanMedia;
import com.ningmeng.framework.domain.course.response.CourseCode;
import com.ningmeng.framework.domain.media.MediaFile;
import com.ningmeng.framework.domain.media.request.QueryMediaFileRequest;
import com.ningmeng.framework.domain.response.QueryResponseResult;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_media.dao.MediaFileRepository;
import com.ningmeng.manage_media.dao.TeachplanMediaRepository;
import com.ningmeng.manage_media.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Administrator on 2020/3/5.
 */
@Service
public class MediaFileService {
    private static Logger logger = LoggerFactory.getLogger(MediaFileService.class);
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    private TeachplanRepository teachplanRepository;
    //文件列表分页查询
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest){
        //查询条件
        MediaFile mediaFile = new MediaFile();
        if(queryMediaFileRequest == null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        //查询条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching() .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())//tag字段 模糊匹配
         .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())//文件原始名称模糊匹配
         .withMatcher("processStatus", ExampleMatcher.GenericPropertyMatchers.exact());// 处理状态精确匹配（默认）
        // 查询条件对象
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        //定义example实例
        Example<MediaFile> ex = Example.of(mediaFile, matcher);
        page = page-1;
        //分页参数
        Pageable pageable = new PageRequest(page, size);
        //分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(ex,pageable);
        QueryResult<MediaFile> mediaFileQueryResult = new QueryResult<MediaFile>();
        mediaFileQueryResult.setList(all.getContent());
        mediaFileQueryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult( CommonCode.SUCCESS,mediaFileQueryResult);
        }
    //保存媒资信息
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia == null){
            ExceptionCast.cast(CommonCode.SERVER_ERROR);
        }
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){ ExceptionCast.cast( CourseCode.COURSE_PUBLISH_PERVIEWISNULL);
        }
        Teachplan teachplan = optional.get();
        //只允许为叶子结点课程计划选择视频
        String grade = teachplan.getGrade();
        if(StringUtils.isEmpty(grade) || !grade.equals("3")){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);
        }
        TeachplanMedia one = null;
        Optional<TeachplanMedia> teachplanMediaOptional = teachplanMediaRepository.findById(teachplanId);
        if(!teachplanMediaOptional.isPresent()){
            one = new TeachplanMedia();
        }else{
            one = teachplanMediaOptional.get();
         }
         //保存媒资信息与课程计划信息
        one.setTeachplanId(teachplanId);
        one.setCourseId(teachplanMedia.getCourseId());
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());
        one.setMediaId(teachplanMedia.getMediaId());
        one.setMediaUrl(teachplanMedia.getMediaUrl());
        teachplanMediaRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    }
