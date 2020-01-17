package com.demo.active.common;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class ConnectionUtils {
    private static Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);

    private static final String CONNECTION_URL = "tcp://119.23.220.195:61616";
    private static Connection connection = null;
    private static Session session = null;

    public static Connection getConnection() throws JMSException {
        if (connection != null) {
            connection.start();
            return connection;
        }
        try {
            return new ActiveMQConnectionFactory(CONNECTION_URL).createConnection();
        } catch (JMSException e) {
            logger.error("create ActiveMQ connection fail......");
            logger.error("error is: " + e.getMessage());
            return null;
        }
    }

    public static boolean closeConnetion() {
        boolean flag = true;
        if (connection == null) {
            logger.error("connction is null");
            return false;
        }
        try {
            connection.close();
        } catch (JMSException e) {
            logger.error("close connection fail......");
            logger.error("error is: " + e.getMessage());
            flag = false;
        }
        return flag;
    }

    public static Session getSession() {
        if (session != null) {
            return session;
        }
        if (connection == null) {
            try {
                connection = new ActiveMQConnectionFactory(CONNECTION_URL).createConnection();
            } catch (JMSException e) {
                logger.error("create Connection fail......");
                logger.error("error is: " + e.getMessage());
                return null;
            }
        }

        try {
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            return session;
        } catch (JMSException e) {
            logger.error("create session fail......");
            logger.error("error is: " + e.getMessage());
            return null;
        }
    }

    public static boolean closeSession() {
        boolean flag = true;
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                flag = false;
                logger.error("session close fail......");
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static void main(String[] args) throws JMSException {
        System.out.println(getConnection());
        System.out.println(getSession());
    }
}
