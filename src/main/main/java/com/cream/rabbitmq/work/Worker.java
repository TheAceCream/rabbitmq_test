package com.cream.rabbitmq.work;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/21.
 */
public class Worker {

    private static final String TASK_QUEUE_NAME = "task_queue";


    public static void main(String[] args) throws IOException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //连接创建通道
        Channel channel = connection.createChannel();
        boolean durable = true;
        //创建队列
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
        final Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("[x] Done");
                }
            }
        };

        boolean autoAck = true;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch:task.toCharArray()){
            if (ch=='.'){
                Thread.sleep(1000);
            }
        }
    }


}
