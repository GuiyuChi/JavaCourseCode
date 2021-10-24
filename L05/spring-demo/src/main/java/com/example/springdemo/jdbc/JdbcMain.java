package com.example.springdemo.jdbc;

import com.example.springdemo.model.User;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 无连接池jdbc调用
 */
public class JdbcMain {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf8";
    private static final String username = "root";
    private static final String password = "1234qwer";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int insert(User user, Connection connection) {
        if (connection == null) {
            connection = getConnection();
        }

        String sql = "insert into user (`name`, age, address) values (?,?,?)";
        PreparedStatement preparedStatement;
        int i = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getAddress());
            i = preparedStatement.executeUpdate();
            preparedStatement.close();
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int update(User user, Connection connection) {
        if (connection == null) {
            connection = getConnection();
        }
        String sql = "update user set `name` = ? where id = ?";
        PreparedStatement preparedStatement;
        int i = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getId());
            i = preparedStatement.executeUpdate();
            preparedStatement.close();
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int delete(int id, Connection connection) {
        if (connection == null) {
            connection = getConnection();
        }

        String sql = "delete from user where id = ?";
        PreparedStatement preparedStatement;
        int i = 0;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            i = preparedStatement.executeUpdate();

            preparedStatement.close();
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static List<User> selectAll(Connection connection) {
        List<User> userList = new ArrayList<>();

        if (connection == null) {
            connection = getConnection();
        }

        String sql = "select * from user";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                user.setAddress(rs.getString("address"));
                userList.add(user);
            }

            statement.close();
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static User selectByName(String name, Connection connection) {
        User user = null;
        if (connection == null) {
            connection = getConnection();
        }
        String sql = "select * from `user` where `name` = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                user.setAddress(rs.getString("address"));
            }
            statement.close();
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        // insert
        User user = new User("张三", 20, "北京");
        insert(user, null);

        // select by name
        user = selectByName("张三", null);
        System.out.println(user);

        // update
        user.setName("李四");
        update(user, null);

        // select all
        List<User> users1 = selectAll(null);
        System.out.println("select all: " + users1);

        // delete
        delete(user.getId(), null);
    }
}
