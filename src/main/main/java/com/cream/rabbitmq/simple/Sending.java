package com.cream.rabbitmq.simple;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/20.
 */
public class Sending {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
       //获取连接
       Connection connection = ConnectionUtil.getConnection();
       //从连接中创建通道
       Channel channel = connection.createChannel();
       //创建队列
       channel.queueDeclare(QUEUE_NAME,false,false,false,null);
       //待传递的消息内容
       String message = "hello world cj";
       channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
       System.out.println("[x] Sent '"+message+"'");
       //关闭连接通道
       channel.close();
       connection.close();
    }

}
