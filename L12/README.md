# 配置 redis 的主从复制，sentinel 高可用，Cluster 集群。
三个redis实例
* redis-0 `localhost:6379`
* redis-1 `localhost:6380`
* redis-2 `localhost:6381`

## 主从复制
`redis-0`作为主库, `redis-1`,`redis-2`作为从库

#### 从库配置
```text
slaveof 127.0.0.1 6379
```

## sentinel 高可用
#### 从库配置
```text
slaveof 127.0.0.1 6379
```

#### sentinel配置
```text
daemonize yes
logfile /home/ubuntu/redis/redis-4.0.8/redis-sentinel.log

sentinel monitor master 127.0.0.1 6379 2
sentinel down-after-milliseconds master 60000
sentinel failover-timeout master 180000
sentinel parallel-syncs master 1
```

## Cluster 集群
每个节点配置文件
```text
# 设置后台启动
daemonize yes
# 开启集群模式
cluster-enabled yes
# 集群节点超时时间(毫秒)
cluster-node-timeout 15000
```
启动每个节点
```shell
./redis-cli --cluster create localhost:6379 localhost:6380 localhost:6381 --cluster-replicas 1
```
验证
```shell
$ ./redis-cli -c -p 6379
127.0.0.1:6379> cluster info
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
...
```
