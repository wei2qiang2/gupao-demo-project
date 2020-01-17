package com.demo.activemq;

import com.demo.active.common.ConnectionUtils;

import javax.jms.*;

/**
 * 消息发送端
 */
public class JMSQueueProducer {

    public static void main(String[] args) throws JMSException {
        Session session = ConnectionUtils.getSession();
        //创建目的地
        Destination queue = session.createQueue("first-queue");
        //创建发送者
        MessageProducer producer = session.createProducer(queue);
        //创建消息
        TextMessage textMessage = session.createTextMessage("hello , this is first producer");
        //发送
        producer.send(textMessage);
        session.commit();
        session.close();
    }
}
