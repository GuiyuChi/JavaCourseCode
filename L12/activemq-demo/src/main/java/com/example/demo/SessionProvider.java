package com.example.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * 会话提供
 *
 */
public class SessionProvider {

    private final static String ENDPOINT
            = "tcp://127.0.0.1:61616";


    private static ActiveMQConnection connection = null;

    private static final ThreadLocal<Session> sessionHolder = new ThreadLocal<>();

    public SessionProvider() {
        try {
            initConnection();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    protected void initConnection() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ENDPOINT);
        ActiveMQConnection conn = (ActiveMQConnection) factory.createConnection();
        conn.start();
        SessionProvider.connection = conn;
    }

    public static Session getSession() {

        if (sessionHolder.get() == null) {
            Session session = null;
            try {
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
            sessionHolder.set(session);
        }

        return sessionHolder.get();

    }

    public static void reset() {
        sessionHolder.remove();
    }

}
