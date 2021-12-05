package com.example.demo.counter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisCounter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void incr(String key) {
        String value = stringRedisTemplate.boundValueOps(key).get();
        if (StringUtils.isEmpty(value)) {
            stringRedisTemplate.boundValueOps(key).set("1");
        } else {
            stringRedisTemplate.boundValueOps(key).increment();
        }
    }

    public String get(String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }
}
