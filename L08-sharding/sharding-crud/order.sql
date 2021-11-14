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
