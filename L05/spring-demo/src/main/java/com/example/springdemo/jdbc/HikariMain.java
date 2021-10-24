package com.example.springdemo.jdbc;

import com.example.springdemo.model.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class HikariMain {
    public static DataSource dataSource() {
        InputStream is = HikariMain.class.getClassLoader().getResourceAsStream("hikari.properties");
        // 加载属性文件并解析：
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HikariConfig hikariConfig = new HikariConfig(props);
        return new HikariDataSource(hikariConfig);
    }

    public static void main(String[] args) throws SQLException {
        // 测试使用连接池查询
        DataSource dataSource = dataSource();
//        System.out.println(dataSource);

        Connection connection = dataSource.getConnection();

        // insert
        User user = new User("张三", 20, "北京");
        JdbcMain.insert(user, connection);

        // select by name
        user = JdbcMain.selectByName("张三", connection);
        System.out.println(user);

        // update
        user.setName("李四");
        JdbcMain.update(user, connection);

        // select all
        List<User> users1 = JdbcMain.selectAll(connection);
        System.out.println("select all: " + users1);

        // delete
        JdbcMain.delete(user.getId(), connection);

    }
}
