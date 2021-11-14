package com.example.demo;

import com.example.demo.entity.Order;
import com.example.demo.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingJdbcTest {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void addOrder() {
        Order order = new Order();
        //order_id由我们设置的策略，雪花算法进行生成

        //分库根据user_id
        order.setUserId(100L);
        order.setCode("order-001");
        orderMapper.insert(order);

        order.setUserId(101L);
        order.setCode("order-002");
        orderMapper.insert(order);
    }

}
