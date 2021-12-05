package com.example.demo;

import com.example.demo.pubsub.Pub;
import com.example.demo.pubsub.Sub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PubTest {
    @Autowired
    Pub pub;

    @Autowired
    Sub sub;

    @Test
    public void pub() throws InterruptedException {
        pub.sendMsg("channal","message1");
    }
}
