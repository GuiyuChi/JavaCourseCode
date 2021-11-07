package com.example.demo.insert;

import java.math.BigDecimal;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;


/**
 * create table `order`
 * (
 * id           bigint auto_increment
 * primary key,
 * code         varchar(64)    null comment '订单编号',
 * receiver     varchar(64)    null comment '收件人',
 * ship_address varchar(128)   null comment '收货地址',
 * ship_phone   varchar(32)    null comment '收货电话',
 * goods_code   varchar(64)    null comment '获取编码',
 * goods_name   varchar(128)   null comment '商品名',
 * goods_num    int            null comment '请求商品数',
 * total_price  decimal(10, 2) null comment '总价',
 * update_time  timestamp      null,
 * create_time  timestamp      null
 * );
 */
public class JdbcService {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/business?useSSL=false&useUnicode=true&characterEncoding=utf8";
    private static final String username = "root";
    private static final String password = "1234qwer";

    private static final String ORDER_CODE_PREFIX = "ORDER-";
    private volatile int index = 1;

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCode() {
        return String.format("%s%d", ORDER_CODE_PREFIX, index++);
    }

    private int insertRows = 1000000;//100万行订单数据

    //订单数据
    private String receiver = "receiver";
    private String ship_address = "ship_address";
    private String ship_phone = "15988888888";
    private String goods_code = "123456";
    private String goods_name = "test-good";
    private Integer goods_num = 1;
    private BigDecimal totalPrice = BigDecimal.ONE;
    private Date nowtime = new Date();
    private Date updateTime = nowtime;
    private Date createTime = nowtime;


    /**
     * 100W normalInsert use 121686 ms
     */
    public void normalInsert() {
        Connection connection = getConnection();
        long begin = System.currentTimeMillis();
        String sql = "insert into `order` (code, receiver,ship_address,ship_phone,goods_code,goods_name,goods_num,total_price,update_time,create_time) values (?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement;
        try {
            connection.setAutoCommit(false);
            for (int i = 0; i < insertRows; i++) {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, getCode());
                preparedStatement.setString(2, receiver);
                preparedStatement.setString(3, ship_address);
                preparedStatement.setString(4, ship_phone);
                preparedStatement.setString(5, goods_code);
                preparedStatement.setString(6, goods_name);
                preparedStatement.setInt(7, goods_num);
                preparedStatement.setBigDecimal(8, totalPrice);
                preparedStatement.setTimestamp(9, new Timestamp(updateTime.getTime()));
                preparedStatement.setTimestamp(10, new Timestamp(createTime.getTime()));
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long spent = System.currentTimeMillis() - begin;
        System.out.printf("normalInsert use %d ms\n", spent);
    }

    /**
     * 批量插入
     * 100w prepare data use 2457 ms batchInsert use 98682 ms
     */
    public void batchInsert() {
        Connection connection = getConnection();
        String headerSql = "insert into `order` (code, receiver,ship_address,ship_phone,goods_code,goods_name,goods_num,total_price) ";
        long begin = System.currentTimeMillis();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (int i = 0; i < insertRows; i++) {
                StringBuffer sql = new StringBuffer();
                sql.append(headerSql);
                sql.append(" values ('")
                        .append(getCode()).append("','")
                        .append(receiver).append("','")
                        .append(ship_address).append("','")
                        .append(ship_phone).append("',")
                        .append(goods_code).append(",'")
                        .append(goods_name).append("',")
                        .append(goods_num).append(",")
                        .append(totalPrice.toString()).append(");");
                statement.addBatch(sql.toString());
            }
            System.out.printf("prepare data use %d ms", System.currentTimeMillis() - begin);

            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long spent = System.currentTimeMillis() - begin;
        System.out.println("");
        System.out.printf("batchInsert use %d ms\n", spent);
    }

    /*
     * 带步长的批量插入
     *
     * 100w prepare data use 1934 ms doAddBatchByStep use 12354 ms
     * @param step
     */
    public void doAddBatchByStep(int step) {
        Connection connection = getConnection();
        long begin = System.currentTimeMillis();

        final String headerSql = "insert into `order` (code, receiver,ship_address,ship_phone,goods_code,goods_name,goods_num,total_price) ";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            for (int i = 0; i < insertRows; i = i + step) {
                StringBuffer sql = new StringBuffer();
                sql.append(headerSql);
                sql.append(" values ");
                for (int j = 0; j < step; j++) {
                    sql.append(" ('")
                            .append(getCode()).append("','")
                            .append(receiver).append("','")
                            .append(ship_address).append("','")
                            .append(ship_phone).append("',")
                            .append(goods_code).append(",'")
                            .append(goods_name).append("',")
                            .append(goods_num).append(",")
                            .append(totalPrice.toString()).append("),");

                }
                sql.deleteCharAt(sql.length() - 1);
                statement.addBatch(sql.toString());
            }
            System.out.printf("prepare data use %d ms\n", System.currentTimeMillis() - begin);
            statement.executeBatch();
            connection.commit();
        } catch (Exception t) {
            t.printStackTrace();
        }
        System.out.println("");
        System.out.printf("doAddBatchByStep use %d ms\n", System.currentTimeMillis() - begin);
    }

}
