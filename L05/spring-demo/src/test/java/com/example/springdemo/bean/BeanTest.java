package com.example.springdemo.bean;

import com.example.springdemo.model.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class BeanTest {

    @Test
    public void case1() {
        // 通过xml配置bean
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = ac.getBean("user1", User.class);
        System.out.println(user);
    }

    @Test
    public void case2() {
        // 通过注解 @Configuration + @Bean
        ApplicationContext ac = new AnnotationConfigApplicationContext("com.example.springdemo.config");
        User user = ac.getBean("user2", User.class);
        System.out.println(user);
    }

}
