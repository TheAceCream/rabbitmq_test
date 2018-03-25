package com.cream.rabbitmq.ps;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/26.
 */
public class EmitLog {

    //设置交换器的名字
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {

        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换器,给它名字,设置交换类型为fanout
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //待传递的消息内容
        String message = getMessage(args);
        //传消息
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
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
