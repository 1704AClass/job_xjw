package com.ningmeng.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2020/2/16.
 */
@Configuration
public class RabbitmqConfig {

    public static  final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_posstpage";


    //交换机
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM()
    {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE). durable (true). build();
    }
}