package com.cream.rabbitmq.routing;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/27.
 */
public class EmitLogDirect {

    //设置交换器的名字
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换器,给它名字,设置交换类型为direct
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //代码里手动设置严重性
        String severity = getSeverity(args);
        //待传递的消息内容
        String message = getMessage(args);
        //发送消息
        channel.basicPublish(EXCHANGE_NAME,severity,null,message.getBytes());
        System.out.println("[x] Sent '"+severity+"':'"+message+"'");
        //关闭连接通道
        channel.close();
        connection.close();
    }

    private static String getSeverity(String[] strings) {
        if (strings.length<1){
            return "info";
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
