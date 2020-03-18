package com.ningmeng.api.webUploader;

import com.ningmeng.framework.domain.media.response.CheckChunkResult;
import com.ningmeng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2020/3/3.
 */
@Api(value = "媒资管理接口",description = "媒资管理接口,提供文件的上传,文件处理等接口")
public interface ModiaUploadControllerApi {

    @ApiOperation("文件上传注册")
    public ResponseResult register(String fileMd5,String fileName,Long fileSize,String mimetype,String fileExt);

    @ApiOperation("分块检查")
    public CheckChunkResult checkchunk(String fileMd5,Integer chunk,Integer chunkSize);

    @ApiOperation("上传分块")
    public ResponseResult uploadchunk(MultipartFile file,Integer chunk,String fileMd5);

    @ApiOperation("合并分块接口")
    public ResponseResult mergechunks(String fileMd5,String fileName,Long fileSize,String mimetype,String fileExt);



}