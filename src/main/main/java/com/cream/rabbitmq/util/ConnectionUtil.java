package com.cream.rabbitmq.util;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by AceCream on 2017/7/20.
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("192.168.20.156");
        //端口
        factory.setPort(5672);
        //填充账户信息
        factory.setVirtualHost("/");
        factory.setUsername("admin");
        factory.setPassword("admin");
        //获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
