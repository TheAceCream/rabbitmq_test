package com.cream.rabbitmq.delay;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AceCream on 2017/11/22.
 */
public class DelayReceiving {

    // 队列名称
    private final static String QUEUE_NAME = "delay_queue";
    private final static String EXCHANGE_NAME="delay_exchange";

    public static void main(String[] argv) throws Exception{
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.queueDeclare(QUEUE_NAME, true,false,false,null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        channel.basicConsume(QUEUE_NAME, true, queueingConsumer);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            System.out.println("****************请等待***************");
            while(true){
                QueueingConsumer.Delivery delivery = queueingConsumer
                        .nextDelivery(); //

                String message = (new String(delivery.getBody()));
                System.out.println("消息:"+message);
                System.out.println("当前时间:\t"+sf.format(new Date()));
            }

        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }
}
