package com.example.demo;

import com.example.demo.datasource.DynamicDataSourceContextHolder;
import com.example.demo.datasource.DynamicDataSourceId;
import com.example.demo.datasource.JdbcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JdbcConfig.class})
public class DynamicDataSourceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @Test
    public void test1() throws SQLException {
        System.out.println(jdbcTemplate.getDataSource() == dataSource); //true
        System.out.println(DataSourceUtils.getConnection(jdbcTemplate.getDataSource())); //com.mysql.jdbc.JDBC4Connection@17503f6b

        List<Map<String, Object>> list1=  jdbcTemplate.queryForList("select * from student");
        System.out.println(list1);

        DynamicDataSourceContextHolder.setDataSourceId(DynamicDataSourceId.SLAVE1);

        System.out.println(jdbcTemplate.getDataSource() == dataSource); //true
        System.out.println(DataSourceUtils.getConnection(jdbcTemplate.getDataSource())); //com.mysql.jdbc.JDBC4Connection@20bd8be5

        List<Map<String, Object>> list2=  jdbcTemplate.queryForList("select * from student");
        System.out.println(list2);


        // 完成操作后  最好把数据源再set回去  否则可能会对该线程后续再使用JdbcTemplate的时候造成影响
        //DynamicDataSourceContextHolder.setDataSourceId(DynamicDataSourceId.MASTER);
    }
}
