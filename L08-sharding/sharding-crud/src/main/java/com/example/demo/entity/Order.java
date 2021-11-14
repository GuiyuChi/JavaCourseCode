package com.example.demo.entity;

import lombok.Data;

@Data
public class Order {
    Long orderId;
    Long userId;
    String code;
}
