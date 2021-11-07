package com.example.demo.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;



public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() { //所有的请求都会走此处，所以没有切换的时候，不要输出日志吧
        String dataSourceId = DynamicDataSourceContextHolder.getDataSourceId();
        if (dataSourceId != null) { //有指定切换数据源切换的时候，才给输出日志 并且也只给输出成debug级别的 否则日志太多了
//            log.debug("线程[{}]，此时切换到的数据源为:{}", Thread.currentThread().getId(), dataSourceId);
        }
        return dataSourceId;
    }

}