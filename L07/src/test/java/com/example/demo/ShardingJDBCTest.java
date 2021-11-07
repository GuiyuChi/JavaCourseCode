package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 使用ShardingSphere 配置 datasource时，需要注释掉JdbcConfig类，避免使用到手动配置的数据源
 */
@SpringBootTest
public class ShardingJDBCTest {
    @Resource
    private DataSource dataSource;

    void selectAll(){
        String sql = "SELECT * FROM student";
        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("student_name"));
            }
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void contextLoads() {
        for(int i=0;i<5;i++){
            selectAll();
        }
    }
}
