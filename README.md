# seata_example

1. 执行script目录下的sql

2. 下载seata-server

3. 启动seata-server

4. 修改jdbc配置

5. 启动order, user, app

6. 调用http://127.0.0.1:8000/call 多调用几次，查看数据库是否回滚成功


```
sudo docker run -d -p 8091:8091 -p 7091:7091 -e SEATA_PORT=8091 -e STORE_MODE=file  --name seata-server apache/seata-server
```
