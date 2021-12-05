package com.example.demo.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Pub {
    @Autowired
    private RedisTemplate redisTemplate;

    public void sendMsg(String channel, String msg) {
        log.info("sendMag,channel:{},msg:{}", channel, msg);
        redisTemplate.convertAndSend(channel, msg);
    }
}
