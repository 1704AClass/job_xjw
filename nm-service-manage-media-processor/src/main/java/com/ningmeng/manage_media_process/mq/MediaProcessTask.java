package com.ningmeng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.ningmeng.framework.domain.media.MediaFile;
import com.ningmeng.framework.domain.media.MediaFileProcess_m3u8;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.utils.HlsVideoUtil;
import com.ningmeng.framework.utils.Mp4VideoUtil;
import com.ningmeng.framework.utils.VideoUtil;
import com.ningmeng.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Administrator on 2020/3/5.
 */
@Component
public class MediaProcessTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);

    @Value( "${nm-service-manage-media.ffmpeg-path}" )
    private String ffmpegPath;

    @Value( "${nm-service-manage-media.video-location}" )
    private String  serverPath;
    @Autowired
    private MediaFileRepository mediaFileRepository;

    @RabbitListener(queues = "${nm-service-manage-media.mq.queue-media-video-processor}",containerFactory = "customContainerFactory")
    public  void  receiveMediaProcessTask(String msg) throws IOException{
        Map map = JSON.parseObject( msg, Map.class );
        //解析消息
        //媒资文件id
        String mediaid = (String)map.get( "mediaid" );
        //获取媒资文件信息
        Optional<MediaFile> optional = mediaFileRepository.findById( mediaid );
        if (!optional.isPresent())
        {
            ExceptionCast.cast( CommonCode.FAIL );
            LOGGER.error( "处理对象不能为空" );
            return;
        }
        MediaFile mediaFile = optional.get();
        //媒资文件类型
        String fileType = mediaFile.getFileType();
        if (fileType==null || !fileType.equals( "avi" ))//目前只处理avi文件
        {
            mediaFile.setProcessStatus( "303004" );//处理状态为无需处理
            mediaFileRepository.save( mediaFile );
        }else{
            mediaFile.setProcessStatus( "303001" );//处理状态为未处理
            mediaFileRepository.save( mediaFile );
        }
        //生成MP4
        String videoPath = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
        String MP4Name = mediaFile.getFileId() + ".MP4";
        String map4folderPath = serverPath + mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil( ffmpegPath, videoPath, MP4Name, map4folderPath );
        String generate = mp4VideoUtil.generateMp4();
        if (generate==null ||!generate.equals( "success" ))
        {
            //操作失败写入处理日志
            mediaFile.setProcessStatus( "303003" );//处理状态为处理失败
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg( generate );
            mediaFile.setMediaFileProcess_m3u8( mediaFileProcess_m3u8 );
            mediaFileRepository.save( mediaFile );
            return;
        }
        //生成m3u8
        videoPath=serverPath+mediaFile.getFilePath()+MP4Name;
        String m3u8Name = mediaFile.getFileId() + ".m3u8";
        String m3u8folderPath = serverPath + mediaFile.getFileId() + ".m3u8";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil( ffmpegPath, videoPath,m3u8Name, m3u8folderPath );
        generate = hlsVideoUtil.generateM3u8();
        if (generate==null||!generate.equals( "success" ))
        {
            mediaFile.setProcessStatus( "303003" );
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg( generate );
            mediaFile.setMediaFileProcess_m3u8( mediaFileProcess_m3u8 );
            mediaFileRepository.save( mediaFile );
            return;
        }
        //获取m3u8列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        mediaFile.setProcessStatus( "303002" );
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist( ts_list );
        mediaFile.setMediaFileProcess_m3u8( mediaFileProcess_m3u8 );
        //m3u8文件url
        mediaFile.setFileUrl( mediaFile.getFilePath()+"hls/"+m3u8Name );
        mediaFileRepository.save( mediaFile );


    }
}
