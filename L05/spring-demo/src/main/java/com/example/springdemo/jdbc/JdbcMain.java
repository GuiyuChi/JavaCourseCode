package com.example.springdemo.jdbc;

import com.example.springdemo.model.User;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 无连接池jdbc调用
 * create table user
 * (
 *     id      bigint auto_increment
 *         primary key,
 *     name    varchar(255) null,
 *     age     int          null,
 *     address varchar(255) null
 * )
 *     charset = utf8;
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

    /**
     * 手动开启事务的insert操作
     *
     * @param user
     * @param connection
     */
    public static void insertWithTransaction(User user, Connection connection) {
        if (connection == null) {
            connection = getConnection();
        }

        Savepoint savepoint = null;

        String sql = "insert into user (`name`, age, address) values (?,?,?)";
        // 开始事务
        try {
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }

        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getAge());
            preparedStatement.setString(3, user.getAddress());
            int updated = preparedStatement.executeUpdate();
//            if(1==1){
//                throw new RuntimeException("testerr");
//            }
            if (updated > 0) {//插入数据成功
                connection.commit();
            } else {
                connection.rollback(savepoint);
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback(savepoint);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 批量插入
     *
     * @param users
     * @param connection
     */
    public static void batchInsert(List<User> users, Connection connection) {
        if (connection == null) {
            connection = getConnection();
        }

        String sql = "insert into user (`name`, age, address) values (?,?,?)";

        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (User user : users) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setInt(2, user.getAge());
                preparedStatement.setString(3, user.getAddress());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            preparedStatement.clearParameters();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        // batch insert
        User user1 = new User("批量1", 20, "北京");
        User user2 = new User("批量2", 20, "北京");
        batchInsert(Arrays.asList(user1, user2), null);

        // select all
        List<User> users = selectAll(null);
        System.out.println("select all: " + users);

        // delete
        for (User deleteUser : users) {
            delete(deleteUser.getId(), null);
        }

        // 事务
        User user3 = new User("事务1", 20, "北京");
        insertWithTransaction(user3,null);

        // select all
        users = selectAll(null);
        System.out.println("select all: " + users);

        // delete
        for (User deleteUser : users) {
            delete(deleteUser.getId(), null);
        }
    }
}
