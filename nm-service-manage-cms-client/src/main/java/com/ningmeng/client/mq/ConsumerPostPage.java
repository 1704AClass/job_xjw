package com.ningmeng.client.mq;

import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2020/2/16.
 */
@Component
public class ConsumerPostPage {

    @RabbitListener(queues = {"${ningmemg.mq.queue}"})
    public  void postpage(String msg)
    {
        Map map= JSON.parseObject(msg,Map.class);

    }
}
