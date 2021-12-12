package com.example.demo;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@SpringBootTest
class DemoApplicationTests {

    @Test
    public void testQueue() throws Exception {
        Destination destination = new ActiveMQQueue("test.queue");
        MessagePublisher publisher = new MessagePublisher(destination);
        for (int i=0 ; i < 10 ; i++) {
            Thread subscriberThread = new Thread(new MessageSubscriber(destination, message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.printf("thread: [%s] received message : %s \n", Thread.currentThread().getName(), textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }));
            subscriberThread.start();
        }


        publisher.sendTextMessage("Hello World");
    }

    @Test
    public void testTopic() throws Exception {
        Destination destination = new ActiveMQTopic("test.topic");
        MessagePublisher publisher = new MessagePublisher(destination);
        for (int i=0 ; i < 10 ; i++) {
            Thread subscriberThread = new Thread(new MessageSubscriber(destination, message -> {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.printf("thread: [%s] received message : %s \n", Thread.currentThread().getName(), textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }));
            subscriberThread.start();
        }


        publisher.sendTextMessage("Hello World");

    }

}
