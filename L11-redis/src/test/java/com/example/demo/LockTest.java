package com.example.demo;

import com.example.demo.lock.RedisLock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class LockTest {
    @Autowired
    private RedisLock redisLock;

    @Test
    public void lockCase() throws InterruptedException {
        String key = "lock1";
        new Thread(() -> {
            try {
                long time = System.currentTimeMillis() + RedisLock.TIMEOUT;
                redisLock.lock(key, String.valueOf(time));
                Thread.sleep(3000);
                System.out.println("unlock");
                redisLock.unlock(key, String.valueOf(time));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        long time1 = System.currentTimeMillis() + RedisLock.TIMEOUT;
        System.out.println(redisLock.lock(key, String.valueOf(time1)));
        Thread.sleep(5000);
        System.out.println(redisLock.lock(key, String.valueOf(time1)));
    }
}
