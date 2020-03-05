package com.ningmeng.manage_media.controller;

import com.ningmeng.api.webUploader.ModiaUploadControllerApi;
import com.ningmeng.framework.domain.media.response.CheckChunkResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 1 on 2020/3/3.
 */
@RestController
@RequestMapping("/media/upload")
class MediaController implements ModiaUploadControllerApi {

    @Autowired
    MediaUploadService mediaUploadService;

    @Override
    //文件上传注册
    @PostMapping("/register")
    public ResponseResult register(@RequestParam("fileMd5") String fileMd5,@RequestParam("fileName") String fileName,@RequestParam("fileSize") Long fileSize,@RequestParam("mimetype") String mimetype,@RequestParam("fileExt") String fileExt) {
        return mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
    }

    @Override
    //分块检查
    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(@RequestParam("fileMd5") String fileMd5,@RequestParam("chunk") Integer chunk,@RequestParam("chunkSize") Integer chunkSize) {
        return mediaUploadService.checkchunk(fileMd5,chunk,chunkSize);
    }

    @Override
    //上传分块
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(@RequestParam("file") MultipartFile file,@RequestParam("chunk") Integer chunk,@RequestParam("fileMd5") String fileMd5) {
        return mediaUploadService.uploadchunk(file,chunk,fileMd5);
    }

    @Override
    //合并分块
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(@RequestParam("fileMd5")String fileMd5,@RequestParam("fileName") String fileName,@RequestParam("fileSize") Long fileSize,@RequestParam("mimetype") String mimetype,@RequestParam("fileExt") String fileExt) {
        return mediaUploadService.mergechunks(fileMd5,fileName,fileSize,mimetype,fileExt);
    }
}
