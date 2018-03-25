package com.cream.rabbitmq.routing;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/27.
 */
public class ReceiveLogsDirect {

    //设置交换器的名字
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换器,给它名字,设置交换类型为direct
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        //得到队列的名字
        String queueName = channel.queueDeclare().getQueue();
        //截一下输入错误的情况
        if (args.length<1){
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }
        //同时我们看看我们到底需要什么程度的日志
        String wholeSeverity = "";
        //根据输入，确定程序想要收集的"严重性"，进行绑定。
        for (String severity : args){
            channel.queueBind(queueName,EXCHANGE_NAME,severity);
            wholeSeverity = wholeSeverity+" "+severity;
        }

        System.out.println("[*] Waiting for"+wholeSeverity+" message.To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("[x] Received'"+envelope.getRoutingKey()+"':'"+message+"'");
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }
}
