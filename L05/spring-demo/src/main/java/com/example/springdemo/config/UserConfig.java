package com.example.springdemo.config;

import com.example.springdemo.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    public User user2(){
        User user = new User();
        user.setId(2);
        user.setName("李四");
        user.setAge(20);
        user.setAddress("上海市");
        return user;
    }
}
