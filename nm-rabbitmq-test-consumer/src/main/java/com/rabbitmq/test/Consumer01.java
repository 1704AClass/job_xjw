package com.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer01 {
    private static final String QUEUE = "helloworld";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory(); //设置MabbitMQ所在服务器的ip和端口
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel(); //声明队列
        channel.queueDeclare(QUEUE, true, false, false, null); //定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //路由key
                String routingKey = envelope.getRoutingKey();
                //消息id
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String msg = new String(body, "utf-8");
                System.out.println("receive message.." + msg);
            }
        }
        channel.basicConsume(QUEUE, true, consumer);
    }
}
