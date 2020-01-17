package com.demo.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息接收端
 *
 * 不能和接受端用同一个Session，否则不能消费到队列的消息
 */
public class JMSQueueConsumerListener {

    public static void main(String[] args) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory("tcp://119.23.220.195:61616").createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("first-queue");
        MessageConsumer consumer = session.createConsumer(destination);

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(Message message) {
                message = (TextMessage)message;
                try {
                    System.out.println(((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };

        while(true){
            consumer.setMessageListener(messageListener);
            session.commit();
        }
//        session.close();
    }
}
