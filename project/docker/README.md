# 根元素博客 - 家宽服务器单机部署文档

基于 `re.lingjiatong.cn` 域名，Docker Compose 单机跑全量服务。
**唯一对外端口：8443**（国内家宽入向 80/443 被封，必须用非标端口）。

## 一、架构

```
                        路由器端口转发（外网 8443 -> 服务器 8443）
                                      |
                            re-entry (openresty, 8443)
            ┌──────────────┬──────────────┬──────────────┬──────────────┐
            |              |              |              |
   re.lingjiatong.cn  admin.re.…      api.re.…        file.re.…
     博客前端:80       后台前端:80    re-gateway:9100   minio:9000
            |                            |
        /artalk/                  re-auth:9104
        artalk:23366              re-service-sys:9111
                                  re-service-article:9112
                                  api-backend:9101
                                  api-file:9103
   内部基础设施（不对外）：
        mysql:3306  redis:6379  nacos:8848(仅回环)  minio:9001(仅内部)
```

| 域名 | 用途 |
| --- | --- |
| `re.lingjiatong.cn:8443` | 博客前台 + `/artalk/` 评论 |
| `admin.re.lingjiatong.cn:8443` | 管理后台（建议再加 Basic Auth） |
| `api.re.lingjiatong.cn:8443` | 后端 API 网关 |
| `file.re.lingjiatong.cn:8443` | Minio，文章图片等静态资源 |

## 二、前置条件

1. **域名**：lingjiatong.cn 已在 DNSPod/阿里云等支持 API 的 DNS 商解析
2. **服务器**：建议 ≥ 8G 内存（6 个 Java 服务各 512M + MySQL/ES 等）
3. **公网 IP**：家宽默认是大内网，打运营商客服要求「公网 IP + 光猫桥接」，路由器 PPPoE 拨号
4. **服务器装好 Docker**（≥ 20.10）与 Compose 插件（`docker compose version` 可用）

## 三、公网链路配置（一次性）

### 1. DDNS
动态公网 IP 变化时自动更新 DNS A 记录。任选其一：
- 路由器自带 DDNS 插件（梅林/OpenWrt 可用 dnsapi 脚本）
- 服务器上跑 [ddns-go](https://github.com/jeessy2/ddns-go)，支持 DNSPod/阿里云 API

需要解析 4 个记录（A 记录，全部指向家中公网 IP）：
```
re.lingjiatong.cn
admin.re.lingjiatong.cn
api.re.lingjiatong.cn
file.re.lingjiatong.cn
```

### 2. 路由器端口转发
外部 TCP `8443` → 服务器内网 IP 的 `8443`。

### 3. HTTPS 证书（acme.sh + DNS-01）
80 端口被封无法走 HTTP-01 验证，用 DNS-01（以 DNSPod 为例）：

```bash
curl https://get.acme.sh | sh
export DP_Id="你的DNSPod TokenId"
export DP_Key="你的DNSPod Token"

# 四个域名一张多域名证书，分别安装到 certs/<域名>/ 目录
acme.sh --issue --dns dns_dp -d re.lingjiatong.cn -d admin.re.lingjiatong.cn \
    -d api.re.lingjiatong.cn -d file.re.lingjiatong.cn

for d in re.lingjiatong.cn admin.re.lingjiatong.cn api.re.lingjiatong.cn file.re.lingjiatong.cn; do
  mkdir -p ~/code/re_backend/project/docker/certs/$d
  acme.sh --install-cert -d re.lingjiatong.cn \
    --fullchain-file ~/code/re_backend/project/docker/certs/$d/fullchain.cer \
    --key-file ~/code/re_backend/project/docker/certs/$d/$d.key
done
```
> acme.sh 的 install-cert 只按签发时的域名对应安装。四个域名共用一张证书时，
> 直接把这个证书文件复制到四个目录、文件名按 `re-entry.conf` 中的要求改好即可。

### 4. 防火墙
```bash
# 只放 8443，其余全部关掉
sudo firewall-cmd --permanent --add-port=8443/tcp && sudo firewall-cmd --reload
```

## 四、部署步骤

### 1. 克隆代码（三个仓库同级）
```bash
mkdir -p ~/code && cd ~/code
git clone <re_backend地址>
git clone <re_admin地址>
git clone <re_frontend地址>
```

### 2. 修改前端生产环境配置
`re_frontend/.env.production`：
```
VITE_API_BASE_URL=https://api.re.lingjiatong.cn:8443/api-frontend
VITE_ARTALK_SERVER=https://re.lingjiatong.cn:8443/artalk
VITE_ARTALK_SITE=re_frontend
```
`re_admin/.env.production`：
```
VITE_API_BASE_URL=https://api.re.lingjiatong.cn:8443
```

### 3. 构建全部镜像
```bash
bash ~/code/re_backend/project/docker/build-frontend.sh
```
> 后端镜像用多阶段构建（容器内 mvn package），首次构建要下载 maven 依赖，耐心等。

### 4. Nacos 配置改容器内网地址（关键步骤！）
配置在 Nacos 命名空间 `f2cc4448-831f-41da-9aa8-b89d4a81d90c` 中，当前指向的是本机开发地址，需要改成容器服务名：

```bash
# 建立隧道（Nacos 控制台只绑定了回环地址）
ssh -L 8848:127.0.0.1:8848 你的服务器
# 浏览器打开 http://localhost:8848/nacos  （账号 nacos / 密码见原配置）
```

逐个检查 `*-prod.yaml`，把以下连接信息改为：
| 原值（开发环境） | 改为 |
| --- | --- |
| `localhost:30601` / `127.0.0.1:3306`（MySQL） | `mysql:3306`，库名 `re` |
| `localhost:30602` / `redis`（Redis） | `redis:6379` |
| Minio 地址 `http://localhost:30606` | `http://minio:9000` |
| Minio 外部访问域名（生成给前端的 URL） | `https://file.re.lingjiatong.cn:8443` |
| Elasticsearch（搜索已改 MySQL 实现，可忽略） | 无需配置 |

> 修改后会自动推送到各服务（@RefreshScope），不放心就 `docker compose restart`。

### 5. 启动
```bash
cd ~/code/re_backend/project/docker
cp .env.example .env   # 按需改密码
docker compose -f docker-compose-server.yml up -d
```
> 首次启动 MySQL 会自动执行初始化（artalk 建库 → 导入 48MB 的 re.sql → 权限路由表），
> 根据磁盘性能可能需要几分钟，看 `docker logs re-mysql` 直到不再刷日志。

### 6. 旧数据图片域名迁移（如果从开发库导入）
开发时文件 URL 存的是 `http://localhost:30606/...`，公网访问需要替换成 Minio 域名：
```sql
UPDATE article SET cover_url = REPLACE(cover_url, 'http://localhost:30606', 'https://file.re.lingjiatong.cn:8443');
UPDATE article SET markdown_content = REPLACE(markdown_content, 'http://localhost:30606', 'https://file.re.lingjiatong.cn:8443');
UPDATE article SET html_content = REPLACE(html_content, 'http://localhost:30606', 'https://file.re.lingjiatong.cn:8443');
-- website_config 等其它含旧地址的表同理
```

## 五、验证清单

```bash
docker compose -f docker-compose-server.yml ps   # 全部应为 running/healthy
curl -k https://api.re.lingjiatong.cn:8443/api-frontend/article/list?pageNum=1&pageSize=1   # 应返回JSON
```

浏览器依次检查：
1. `https://re.lingjiatong.cn:8443` 博客首页、文章详情、评论框加载
2. `https://admin.re.lingjiatong.cn:8443` 后台登录、发文章
3. 文章图片正常显示（走 file.re 域名）

## 六、运维

```bash
cd ~/code/re_backend/project/docker
docker compose -f docker-compose-server.yml logs -f re-gateway   # 看日志
docker compose -f docker-compose-server.yml up -d --build <服务名> # 更新某个后端服务
docker compose -f docker-compose-server.yml pull artalk && docker compose up -d artalk
```

**更新代码全流程**：三个仓库 `git pull` → 重新跑 `build-frontend.sh`（或单独 build 变更的镜像）→ `docker compose up -d`（镜像变了的容器会自动重建）。

## 七、安全清单

- [x] 对外只开 8443，MySQL/Redis/Minio/Nacos 不映射公网
- [x] Nacos 控制台、网关仅绑定 `127.0.0.1`，管理走 ssh 隧道
- [ ] `admin.re.lingjiatong.cn` 开启 `auth_basic`（`re-entry.conf` 中已留好注释）或路由器侧限制来源 IP
- [ ] `.env` 中所有默认密码换成强密码（NACOS_AUTH_TOKEN 改后要重新 base64 六次）
- [ ] Minio 控制台（9001 端口）默认不对外，需要管理时临时映射到回环 + ssh 隧道

## 八、常见问题

**Q: 部分地区连 8443 也封？**
路由器转发改用其它端口（如 10443），同步改 `re-entry.conf` 的 `listen` 和 compose 的 ports 映射即可。前端 `.env.production` 里的地址也要带上新端口重新构建。

**Q: 评论框不显示/报跨域？**
Artalk 的 `ATK_TRUSTED_DOMAINS` 必须与浏览器地址栏完全一致（含 `https` 和 `:8443`）。

**Q: 图片 403？**
Minio presigned URL 校验 Host，`re-entry.conf` 中 file 域已加 `proxy_buffering off` 并透传 Host；
bucket 由后端启动时自动创建，若失败看 `api-file` 日志。

**Q: DDNS 生效但访问不通？**
多数是路由器防火墙/光猫没真桥接；先在服务器上 `curl -k https://127.0.0.1:8443` 确认本机通，再排查外网链路。
