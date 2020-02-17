package com.ningmeng.client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2020/2/16.
 */
@Configuration
public class Rabbitmqconfig {

    public static  final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_posstpage";

    public static final String QUEUE_CMS_POSTPAGE="queue_cms_postpqga";

    @Value("${ningmeng.mq.queue}")
    public String queue_cms_postpage_name;
    @Value("${ningmeng.mq.routingKey}")
    public String routingKey;
    //交换机
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM()
    {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE). durable (true). build();
    }
    //队列
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue QUEUE_TOPICS_POSTPAGE()
    {
        Queue queue = new Queue(queue_cms_postpage_name);
        return queue;

    }
    //绑定交换机和队列
    @Bean
    public Binding BLNUING_QUEUE_INF0RML_SNS (@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange,@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
