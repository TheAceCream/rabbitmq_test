package com.cream.rabbitmq.ps;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/26.
 */
public class ReceiveLogs {
    //设置交换器的名字
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //声明交换器,给它名字,设置交换类型为fanout
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //得到队列的名字
        String queueName = channel.queueDeclare().getQueue();
        //队列和交换器进行绑定
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("[*] Waiting for message.To exit press CTRL+C");

        Consumer consumer =new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("[x]Receive '"+ message+"'");
            }
        };
        boolean autoAck = true;

        channel.basicConsume(queueName,autoAck,consumer);



    }
}
