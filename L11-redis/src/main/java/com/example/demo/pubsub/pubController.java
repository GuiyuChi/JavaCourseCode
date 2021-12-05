package com.example.demo.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class pubController {

    @Autowired
    private Pub redisPub;

    @GetMapping("/test/send")
    public Object sendMsg(String data) {
        redisPub.sendMsg("channel", data);
        return "success";
    }
}
