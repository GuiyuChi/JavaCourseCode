-- 商品表
create table goods
(
    id         bigint auto_increment
        primary key,
    code       varchar(64)    null comment '商品编号',
    goods_name varchar(256)   null comment '商品名称',
    price      decimal(10, 2) null comment '单价'
);

-- 订单表
create table `order`
(
    id           bigint auto_increment
        primary key,
    code         varchar(64)    null comment '订单编号',
    receiver     varchar(64)    null comment '收件人',
    ship_address varchar(128)   null comment '收货地址',
    ship_phone   varchar(32)    null comment '收货电话',
    goods_code   varchar(64)    null comment '获取编码',
    goods_name   int            null comment '商品名',
    goods_num    int            null comment '请求商品数',
    total_price  decimal(10, 2) null comment '总价',
    update_time  timestamp      null,
    create_time  timestamp      null
);

-- 用户表
create table user
(
    id          bigint auto_increment
        primary key,
    code        varchar(64)  null comment '用户编码',
    name        varchar(128) null comment '用户名',
    phone       varchar(32)  null comment '电话',
    update_time timestamp    null,
    create_time timestamp    null
);

