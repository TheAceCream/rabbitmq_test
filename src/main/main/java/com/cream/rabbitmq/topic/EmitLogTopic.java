package com.cream.rabbitmq.topic;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/28.
 */
public class EmitLogTopic {

    //设置交换器的名字
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args){
        Connection connection = null;
        Channel channel = null;
        try {
        //获取连接
        connection = ConnectionUtil.getConnection();
        //创建通道
        channel = connection.createChannel();
        //声明交换器,给它名字,设置交换类型为direct
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        //代码里手动设置路由键——RoutingKey
        String routingKey = getRouting(args);
        //待传递的消息内容
        String message = getMessage(args);

        //发送消息
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes("UTF-8"));
        System.out.println("[x] Sent '"+routingKey+"':'"+message+"'");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭连接
            if (connection!=null){
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getRouting(String[] strings) {
        if (strings.length<1){
            return "anonymous.info";
        }
        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length<2){
            return "hello world";
        }
        return joinStrings(strings," ",1);
    }

    private static String joinStrings(String[] strings, String delimiter,int startIndex) {
        int length = strings.length;
        if (length == 0){
            return "";
        }
        if (length<startIndex){
            return "";
        }
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex+1 ; i < length; i++){
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }


}
