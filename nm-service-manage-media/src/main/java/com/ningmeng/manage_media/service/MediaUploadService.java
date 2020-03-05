package com.ningmeng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.netflix.discovery.converters.Auto;
import com.ningmeng.framework.domain.cms.response.CmsCode;
import com.ningmeng.framework.domain.media.MediaFile;
import com.ningmeng.framework.domain.media.response.CheckChunkResult;
import com.ningmeng.framework.domain.media.response.MediaCode;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_media.dao.MediaFileRepository;
import com.ningmeng.manage_media_process.config.RabbitMQConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2020/3/3.
 */
@Service
public class MediaUploadService {
    @Autowired
    private MediaFileRepository mediaFileRepository;

    //上传文件根目录
    @Value( "${nm-service-manage-media.upload-location}" )
    private String  uploadpath;
    @Value( "${nm-service-manage-media.mq.routingkey-media-video}" )
    private String routingkey_media_video;
    @Autowired
    private RestTemplate restTemplate;
    private RabbitTemplate rabbitTemplate;

    /**
     *根据文件md5得到文件路径
     *规则：
     *一级目录：md5的第一个字符
     *二级目录：md5的第二个字符
     *三级目录：md5
     *文件名：md5+文件扩展名
     *@param
     fileMd5
     文件md5值
     *@param
     fileExt
     文件扩展名
     *@return 文件路径
     */
    private  String getfilepath(String fileMd5,String fileExt )
    {
        return uploadpath+"/"+fileMd5.substring(0,1)+"/"+fileMd5.substring( 1,2 )+"/"+fileMd5+"/"+fileMd5+"."+fileExt;
    }
    //得到文件的相对路径
    private String getFileFolderRelativePath(String fileMd5,String fileExt)
    {
        return fileMd5.substring(0,1)+"/"+fileMd5.substring( 1,2 )+"/"+fileMd5+"/";
    }
    //得到文件的所在目录
    private String getFileFolderPath(String fileMd5)
    {
        return uploadpath+"/"+fileMd5.substring(0,1)+"/"+fileMd5.substring( 1,2 )+"/"+fileMd5+"/";
    }
    //创建文件目录
    private boolean createFileFold(String fileMd5)
    {
        //创建上传文件目录
        String fileFolderPath = getFileFolderPath( fileMd5 );
        File file = new File( fileFolderPath );
        if (!file.exists())
        {
            //创建文件
            boolean mkdir = file.mkdirs();
            return mkdir;
        }
        return true;
    }
    private File[] getChunkFiles(File chunkfileFolder)
    {
        File[] files = chunkfileFolder.listFiles();
        Arrays.sort( files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt( o1.getName() )>Integer.parseInt( o2.getName() ))
                return 1;
                return -1;
            }

        } );
        return files;
    }
    private File mergeFile(File mergeFile,File[] chunkFiles)
    {
        try {
        //用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //分块列表
        File[] fileArray = mergeFile.listFiles();
        // 转成集合，便于排序
         List<File> fileList = new ArrayList<File> (Arrays.asList(fileArray));
        //合并文件
        for(File chunkFile:fileList){
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"rw");
            int len =-1;
            while((len=raf_read.read(b))!=-1){
                    raf_write.write(b,0,len);
            }
                raf_read.close();
        }
        raf_write.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  mergeFile;
    }
    //校验md5
    private boolean checkfileMd5(File mergefile,String md5)
    {
        InputStream inputStream=null;
        try {
            inputStream=new FileInputStream( mergefile );
            String s = DigestUtils.md5Hex( inputStream );
            if (s.equalsIgnoreCase( md5 ))
            {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(inputStream!=null)
                {
                    inputStream.close();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
    //向mq发送视频处理信息
    private boolean  sendProcessVideoMsg(String mediaId){
        Map<String,String> msgMap = new HashMap<>();
        msgMap.put("mediaId",mediaId);
//发送的消息
        String msg = JSON.toJSONString(msgMap);
        try {
            this.rabbitTemplate.convertAndSend( RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,msg);
            return true;
       }catch (Exception e){
            return false;
       }
    }
    //文件上传注册
    public ResponseResult register(String fileMd5, String fileName, Long filesize, String mimetype, String fileExt)
    {
        //检查文件是否上传
        //1、得到文件的路径
        String getfilepath = this.getfilepath( fileMd5, fileExt );
        File file = new File( getfilepath );
        //2、查询文件是否在数据库中存在
        Optional<MediaFile> mediaFile = mediaFileRepository.findById( fileMd5 );
        if (mediaFile.isPresent())
            if (file.exists()) {
                ExceptionCast.cast( MediaCode.UPLOAD_FILE_REGISTER_EXIST );
            }
        boolean fileFold = this.createFileFold( fileMd5 );
        if (!fileFold)
        {
           ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult( CommonCode.SUCCESS );
    }
    //分块检查
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunksize){

        String fileFolderPath = this.getFileFolderPath( fileMd5 );
        File file = new File( fileFolderPath );
        if (file.exists())
        {
            return new CheckChunkResult( MediaCode.CHUNK_FILE_EXIST_CHECK,true );
        }else {
            return new CheckChunkResult( null,false );
        }
    }
    //上传
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5)
    {
        if (file==null)
        {
            ExceptionCast.cast( MediaCode.UPLOAD_FILE_REGISTER_ISNULL );
        }
        boolean fileFold = this.createFileFold( fileMd5 );
        File file1 = new File( this.getFileFolderPath( fileMd5 ) + chunk );
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(file1);
            IOUtils.copy(inputStream,outputStream);
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ResponseResult( CommonCode.FAIL );
        }finally {
            try {
                if (inputStream!=null) {
                    inputStream.close();
                }
                if (outputStream!=null)
                {
                    outputStream.close();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return new ResponseResult( CommonCode.SUCCESS);
    }
    //合并文件
    public ResponseResult mergechunks(String fileMd5,String fileName,Long filesize,String mimetype,String fileExt)
    {
        //获取块文件的路径
        String chunkfileFolderPath = this.getFileFolderPath( fileMd5 );
        File chunkfileFolder =new File(chunkfileFolderPath);
        if(!chunkfileFolder.exists()){
                chunkfileFolder.mkdirs();
        }
        //合并文件
        File file = new File( this.getfilepath( fileMd5, fileExt ) );
        //创建合并文件,如果合并文件已存在先删除再创建
        if (file.exists())
        {
            file.delete();
        }
        boolean newfile=false;
        try {
            newfile=file.createNewFile();
        }catch (Exception e)
        {
            e.printStackTrace();

        }
        if (!newfile)
        {
            ExceptionCast.cast( MediaCode.MERGE_FILE_FAIL );
        }
        File[] chunkFiles = this.getChunkFiles( chunkfileFolder );
         file = this.mergeFile( file, chunkFiles );
         if (file==null)
         {
             ExceptionCast.cast( MediaCode.MERGE_FILE_FAIL );
         }
         //校验md5
        boolean b = this.checkfileMd5( file, fileMd5 );
         if (!b)
         {
             ExceptionCast.cast( MediaCode.CHUNK_FILE_EXIST_CHECK );
         }
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5,fileExt));
        mediaFile.setFileSize(filesize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save( mediaFile );
        boolean msg = this.sendProcessVideoMsg( fileMd5 );
        if (!msg)
        {
            ExceptionCast.cast( MediaCode.CHUNK_FILE_EXIST_CHECK );
        }
        return new ResponseResult( CommonCode.SUCCESS );

    }

}