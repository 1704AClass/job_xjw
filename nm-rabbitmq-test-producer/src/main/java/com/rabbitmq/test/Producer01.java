package com.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer01 {
    //队列名称
    private static final String QUEUE = "helloworld";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");//rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务 器 //创建与RabbitMQ服务的TCP连接
             connection = factory.newConnection(); //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
             channel = connection.createChannel();
             channel.queueDeclare(QUEUE, true, false, false, null);
             String message = "helloworld小明"+System.currentTimeMillis();
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("Send Message is:'" + message + "'");
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            if(channel != null) {
                channel.close();
            }
                if(connection != null) {
                connection.close();
            }
        }
    }
}