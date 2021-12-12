package com.example.demo;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import java.util.function.Consumer;

/**
 * 消息订阅方
 */
public class MessageSubscriber implements MessageListener, Runnable {

    private final Destination destination;
    private final Session session;

    private final Consumer<Message> consumer;

    public MessageSubscriber(Destination destination, Consumer<Message> consumer) {
        this.destination = destination;
        this.consumer = consumer;
        this.session = SessionProvider.getSession();
    }

    @Override
    public void run() {
        try {
            MessageConsumer consumer = session.createConsumer(this.destination);
            consumer.setMessageListener(this);

            while (true) {
                // 阻塞线程
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(Message message) {
        this.consumer.accept(message);
    }
}
