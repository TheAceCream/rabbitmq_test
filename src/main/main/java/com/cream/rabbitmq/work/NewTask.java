package com.cream.rabbitmq.work;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/21.
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel = connection.createChannel();
        //创建队列
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);
        //待传递的消息内容
        String message = getMessage(args);
        //传送
        channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println("[x] Sent '"+message+"'");
        //关闭连接通道
        channel.close();
        connection.close();

    }

    private static String getMessage(String[] strings) {
        if (strings.length<1){
            return "hello world";
        }
        return joinStrings(strings," ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0){
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1;i < length; i++){
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }

}
