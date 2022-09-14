# minio数据迁移

minio要做数据迁移需要借助第三方工具rclone。
rclone是一个开源的对象存储在线迁移工具，用于文件和目录的同步，支持阿里云的oss、minio 、亚马逊S3 等。

## 注意事项

1、两台机器的时区及时间要保持一致，最后进行迁移之前，两台机器的时间进行校准。方法如下：

```bash

timedatectl set-timezone Asia/Shanghai

ntpdate ntp.aliyun.com

```

## 部署过程

### rclone安装

```bash

curl https://rclone.org/install.sh | sudo bash

```


## 生成配置文件

编辑/root/.config/rclone/rclone.conf

```bash

[minio-new]
type = s3
provider = Minio
env_auth = false
access_key_id = lingjiatong
secret_access_key = re#minio2022
endpoint = http://192.168.31.132:30090
[minio-old]
type = s3
provider = Minio
env_auth = false
access_key_id = lingjiatong
secret_access_key = re#minio2022
endpoint = http://192.168.31.114:30090

```

* [minio-new] 是定义对minio集群的简称
* [minio-local] 是对应对本地minio的简称
* access_key_id minio的登录用户名
* secret_access_key minio的登录密码
* endpoint minio地址


## 同步数据

```bash

rclone sync minio-old:rootelement minio-new:rootelement

```

如上命令的意思是将minio-local的dosm桶同步到minio-new的dosm桶(如果目标minio上没有对应的桶 会在同步的过程中自动创建)

