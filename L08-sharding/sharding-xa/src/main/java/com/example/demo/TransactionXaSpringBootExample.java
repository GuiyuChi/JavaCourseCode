
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootApplication
@Import(TransactionConfiguration.class)
public class TransactionXaSpringBootExample {
    
    @Resource
    private XAOrderService orderService;
    
    public static void main(final String[] args) {
        SpringApplication.run(TransactionXaSpringBootExample.class, args);
    }
    
    @PostConstruct
    public void executeOrderService() {
        orderService.init();
        try{
            orderService.insertFailed(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(orderService.selectAll());
        orderService.cleanup();
    }
}
