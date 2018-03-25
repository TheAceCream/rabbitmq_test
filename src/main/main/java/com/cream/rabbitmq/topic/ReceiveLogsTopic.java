package com.cream.rabbitmq.topic;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/28.
 */
public class ReceiveLogsTopic {

    //设置交换器的名字
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Connection connection = null;
        Channel channel = null;
        //获取连接
        connection = ConnectionUtil.getConnection();
        //创建通道
        channel = connection.createChannel(7);
        //声明交换器,给它名字,设置交换类型为topic
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //得到队列的名字
        String queueName = channel.queueDeclare().getQueue();
        //截一下输入错误的情况
        if (args.length<1){
            System.err.println("Usage: ReceiveLogsTopic [binding_key]");
            System.exit(1);
        }
        String wholeSeverity = "";
        //根据输入，确定程序想要收集的"RoutingKey"，进行绑定。
        for (String bindingKey : args){
            channel.queueBind(queueName,EXCHANGE_NAME,bindingKey);
            wholeSeverity = wholeSeverity + " " + bindingKey;
        }
        System.out.println("[*] Waiting for"+wholeSeverity+" message.To exit press CTRL+C");
        //消费者
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
