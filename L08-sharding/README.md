2.（必做）设计对前面的订单表数据进行水平分库分表，拆分 2 个库，每个库 16 张表。并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

代码位置: sharding-crud  
分库结构简化为每个库下两个表，原理和16张表相同  
```
create database order_db_1;
create database order_db_2;

use order_db_1;

create table order_1 (
    order_id   bigint   primary key,
    user_id    bigint    not null comment '用户id',
    code       varchar(64)    not null comment '订单编号'
);

create table order_2 (
    order_id   bigint   primary key,
    user_id    bigint    not null comment '用户id',
    code       varchar(64)    not null comment '订单编号'
);

use order_db_2;

create table order_1 (
    order_id   bigint   primary key,
    user_id    bigint    not null comment '用户id',
    code       varchar(64)    not null comment '订单编号'
);

create table order_2 (
    order_id   bigint   primary key,
    user_id    bigint    not null comment '用户id',
    code       varchar(64)    not null comment '订单编号'
);

```