package com.cream.rabbitmq.delay;

import com.cream.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AceCream on 2017/11/22.
 */
public class DelaySending {

    // 队列名称
    private final static String EXCHANGE_NAME="delay_exchange";
    private final static String ROUTING_KEY="key_delay";

    @SuppressWarnings("deprecation")
    public static void main(String[] argv) throws Exception {
        /**
         * 创建连接连接到MabbitMQ
         */
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        // 声明x-delayed-type类型的exchange
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        channel.exchangeDeclare(EXCHANGE_NAME, "x-delayed-message", true, false, args);

        Map<String, Object> headers = new HashMap<String, Object>();
        //设置在2016/11/04,16:45:12向消费端推送本条消息
        Date now = new Date();
        Date timeToPublish = new Date("2017/12/5,16:08:3");

        String readyToPushContent = "在 " + sf.format(now) + "发布"
                + " \t 在 " + sf.format(timeToPublish)+" 传递";

        headers.put("x-delay", timeToPublish.getTime() - now.getTime());

        AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder()
                .headers(headers);
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, props.build(),
                readyToPushContent.getBytes());

        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
