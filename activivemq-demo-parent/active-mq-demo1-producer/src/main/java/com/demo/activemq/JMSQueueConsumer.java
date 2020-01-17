package com.demo.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息接收端
 *
 * 不能和接受端用同一个Session，否则不能消费到队列的消息
 */
public class JMSQueueConsumer {

    public static void main(String[] args) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory("tcp://119.23.220.195:61616").createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("first-queue");
        MessageConsumer consumer = session.createConsumer(destination);
        TextMessage receive = (TextMessage) consumer.receive();

        String text = receive.getText();

        System.out.println("consumer consume textmessage: " + text);

        session.commit();
        session.close();
    }
}
