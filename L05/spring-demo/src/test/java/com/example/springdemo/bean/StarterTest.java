package com.example.springdemo.bean;

import com.example.springdemo.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StarterTest {
    @Autowired
    Student student;

    @Test
    public void case1(){
        System.out.println(student);
    }
}
