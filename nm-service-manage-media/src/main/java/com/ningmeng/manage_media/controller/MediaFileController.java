package com.ningmeng.manage_media.controller;

import com.ningmeng.api.webUploader.MediaFileControllerApi;
import com.ningmeng.framework.domain.course.TeachplanMedia;
import com.ningmeng.framework.domain.media.request.QueryMediaFileRequest;
import com.ningmeng.framework.domain.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2020/3/5.
 */
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {
    @Autowired
    private MediaFileService mediaFileService;
    @Override
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, @PathVariable("queryMediaFileRequest") QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileService.findList( page,size,queryMediaFileRequest );
    }

    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return mediaFileService.savemedia( teachplanMedia );
    }
}
