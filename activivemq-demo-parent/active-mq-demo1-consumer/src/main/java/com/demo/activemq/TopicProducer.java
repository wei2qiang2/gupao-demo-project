package com.demo.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicProducer {

    public static void main(String[] args) throws JMSException {

        Connection connection = new ActiveMQConnectionFactory("tcp:119.23.220.195:61616").createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination topic = session.createTopic("first-topic");
        MessageProducer producer = session.createProducer(topic);

        TextMessage textMessage = session.createTextMessage("message content......");

        producer.send(textMessage);
        session.commit();
        session.close();
    }
}
