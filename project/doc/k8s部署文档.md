# 使用k8s集群部署根元素博客

## 部署环境

操作系统：CentOS Linux release 7.9.2009 (Core)

linux内核版本：

k8s集群版本：v1.22.10

k8s集群配置：

* master（192.168.31.113）
* node（192.168.31.113）

## 安装步骤

### 1、NFS安装

根元素博客系统使用静态PV形式，有状态服务以statefulset形式部署单机模式，使用静态PV的形式存储数据。k8s集群的PV用的是nfs，所以需要先安装好nfs并且预先分配好指定的目录。

```bash
# 安装nfs
yum install -y nfs-utils rpcbind

# 启动nfs服务
systemctl start nfs && systemctl enable nfs
systemctl start rpcbind && systemctl enable rpcbind

# 创建目录
mkdir -p /home/nfs/mysql/data
mkdir -p /home/nfs/mysql/log
mkdir -p /home/nfs/nacos/data
mkdir -p /home/nfs/nacos/log
mkdir -p /home/nfs/redis/data
mkdir -p /home/nfs/es/data
mkdir -p /home/nfs/es/log
mkdir -p /home/nfs/minio/data
mkdir -p /home/nfs/minio/config

# 给目录授权
chmod 777 -R /home/nfs/mysql/data
chmod 777 -R /home/nfs/mysql/log
chmod 777 -R /home/nfs/nacos/data
chmod 777 -R /home/nfs/nacos/log
chmod 777 -R /home/nfs/redis/data
chmod 777 -R /home/nfs/es/log
chmod 777 -R /home/nfs/es/data
chmod 777 -R /home/nfs/minio/data
chmod 777 -R /home/nfs/minio/config

# 编辑/etc/exports文件内容如下
# mysql
/home/nfs/mysql/data 192.168.31.0/24(rw,no_root_squash,sync)
/home/nfs/mysql/log 192.168.31.0/24(rw,no_root_squash,sync)
# nacos
/home/nfs/nacos/data 192.168.31.0/24(rw,no_root_squash,sync)
/home/nfs/nacos/log 192.168.31.0/24(rw,no_root_squash,sync)
# redis
/home/nfs/redis/data 192.168.31.0/24(rw,no_root_squash,sync)
# elasticsearch
/home/nfs/es/data 192.168.31.0/24(rw,no_root_squash,sync)
/home/nfs/es/log 192.168.31.0/24(rw,no_root_squash,sync)
# minio
/home/nfs/minio/data 192.168.31.0/24(rw,no_root_squash,sync)
/home/nfs/minio/config 192.168.31.0/24(rw,no_root_squash,sync)

# 更新配置
exportfs -ar

```

### 2、执行k8s部署相关yml

注意不要将re-pv.yml中的accessMode改为ReadWriteMany，不然无法使用nfs作为pv

```bash

# 创建k8s名称空间
kubectl apply -f re-namespace.yml
# 创建k8s的configmap对象
kubectl apply -f re-configmap.yml
# 创建k8s的pv对象，注意这里需要根据nfs服务的ip地址进行改动
kubectl apply -f re-pv.yml
# 部署mysql
kubectl apply -f mysql.yml
# 部署mysql之后，为了后面部署nacos，需要使用工具连接mysql执行nacos数据库初始化脚本
# 部署nacos
kubectl apply -f nacos.yml
# 部署redis
kubectl apply -f redis.yml
# 部署elasticsearch
# 部署es之前，最好在每个k8s节点上配置一下最大文件打开数，避免es部署出现问题
echo "vm.max_map_count=262144" >> /etc/sysctl.conf
sysctl -p /etc/sysctl.conf
kubectl apply -f elasticsearch.yml
# 部署minio
kubectl apply -f minio.yml

```


### k8s安装ingress博客

https://github.com/anjia0532/gcr.io_mirror