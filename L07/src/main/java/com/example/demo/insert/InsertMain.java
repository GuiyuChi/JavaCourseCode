package com.example.demo.insert;

public class InsertMain {
    public static void main(String[] args) {
        JdbcService jdbcService = new JdbcService();
        jdbcService.doAddBatchByStep(500);
    }
}
