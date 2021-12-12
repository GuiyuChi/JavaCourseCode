package com.example.demo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MessagePublisher {

    private final Destination destination;
    private final MessageProducer producer;
    private final Session session;

    public MessagePublisher(Destination destination) throws JMSException {
        this.destination = destination;
        SessionProvider sessionProvider = new SessionProvider();
        this.session = sessionProvider.getSession();
        this.producer = this.session.createProducer(this.destination);
    }

    public void sendTextMessage(String message) throws JMSException {
        TextMessage textMessage = this.session.createTextMessage(message);
        this.producer.send(textMessage);
    }

}
