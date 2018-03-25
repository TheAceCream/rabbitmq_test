package com.cream.rabbitmq.simple;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/20.
 */
public class Receiving {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //创建队列
        //exclusive ：是否为当前连接的专用队列，在连接断开后，会自动删除该队列，生产环境中很少用到。
        //autodelete：当没有任何消费者使用时，自动删除该队列。
        //arguments: 其他参数 可以限制队列的大小、消息的死信时间、优先级等等
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义队列的消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }

}
