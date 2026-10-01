# 根元素博客 - 服务器单机部署文档

在 `docker-compose-local.yml`（本地一键部署）基础上扩展：中间件端口映射与开发环境完全一致，
新增后端 6 个微服务和 re-frontend / re-admin 前端。
服务器通过家宽公网 IP + DDNS 提供访问，路由器按下方端口表做端口转发即可。

## 一、端口一览

| 容器 | 宿主端口 | 说明 |
| --- | --- | --- |
| re-mysql | 30601 | MySQL |
| re-redis | 30602 | Redis |
| re-nacos | 30603 / 31603 / 30605 | Nacos 控制台 / gRPC / 控制台新版端口 |
| re-minio | 30606 / 30607 | S3 API / 控制台 |
| re-elasticsearch | 30608 / 30609 | ES |
| re-artalk | 30610 | 评论系统 |
| **re-gateway** | **9100** | 后端网关（前端 API 入口） |
| re-auth | 9104 | 认证服务 |
| api-backend | 9101 | 后台管理 API |
| api-frontend | 9102 | 博客前台 API |
| api-file | 9103 | 文件服务 |
| re-service-sys | 9111 | 系统服务 |
| re-service-article | 9112 | 文章服务 |
| **re-frontend** | **30151** | 博客前台 |
| **re-admin** | **30150** | 管理后台 |

## 二、本地一键部署

```bash
cd re_backend/project/docker
docker compose -f docker-compose-local.yml up -d
```

中间件端口与开发环境一致（30601-30610），首次启动（空数据目录）自动执行 `init-sql/init.sql`：
1. 创建 artalk / nacos / re 三个库
2. nacos 库：建表 + prod 命名空间 + **各微服务的运行配置**（config_info 表，容器内网地址版）
3. re 库：建表 + 默认用户/角色/权限/菜单/路由数据

> 注意：与 re_local 仓库的 compose 是等价的二选一关系，端口和容器名相同，**不要同时跑**。

## 三、公网链路（一次性）

1. **公网 IP**：打运营商客服要「公网 IP + 光猫桥接」，路由器 PPPoE 拨号
2. **DDNS**：用 DNSPod/阿里云 API 把 `re.lingjiatong.cn` 解析到家中动态 IP
   （ddns-go 或路由器插件均可）
3. **端口转发**：路由器把上表需要对外的端口转发到服务器，最小集：
   `30151`（博客）、`30150`（后台）、`9100`（API）、`30610`（评论）、`30606`（文章图片，Minio）
4. **HTTPS（可选）**：家宽 80/443 被封，要 HTTPS 就先用非标端口跑起来，
   再用 acme.sh DNS-01（DNSPod API）签证书，然后自行套一层 nginx/caddy 反代或在路由器终结 TLS

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
VITE_API_BASE_URL=http://re.lingjiatong.cn:9100/api-frontend
VITE_ARTALK_SERVER=http://re.lingjiatong.cn:30610
VITE_ARTALK_SITE=re_frontend
```
`re_admin/.env.production`：
```
VITE_API_BASE_URL=http://re.lingjiatong.cn:9100
```
> 这里的域名+端口就是外网访问的地址，按 DDNS 实际情况写。

### 3. 构建全部镜像
```bash
bash ~/code/re_backend/project/docker/build-frontend.sh
```
> 后端先用 maven 打包 jar，再使用各服务自己的 Dockerfile 构建镜像（服务器上需 JDK21 + maven + docker）。

**镜像版本规则**：所有应用镜像按各自的应用版本号打 tag，不再是 `latest`——
后端取 `re_backend/pom.xml` 的 `<re-version>`，前端取各自 `package.json` 的 `version`。
构建脚本会把版本号写入本目录的 `.env`（`RE_VERSION` / `RE_ADMIN_VERSION` / `RE_FRONTEND_VERSION`），
compose 通过 `${...}` 引用。发新版时先改 pom.xml / package.json 的版本号再构建即可。

### 4. Nacos 配置（初始化在 init.sql，一般无需操作）
- Nacos 使用 MySQL 存储（不再用内嵌 derby），库表与初始配置由 `init-sql/init.sql` 写入：
  prod 命名空间（`f2cc4448-831f-41da-9aa8-b89d4a81d90c`）下的 7 个 `*-prod.yaml`
  （数据源、redis、minio、es、sa-token 等，**已是容器内网地址**：`re-mysql:3306`、
  `re-redis:6379`、`re-minio:9000`、`re-elasticsearch:9200`）
- 要修改配置：直接在 Nacos 控制台编辑（持久化到 MySQL，立即生效或 restart 后生效），
  同时建议同步修改 `init-sql/init.sql` 里对应的 `config_info` 记录，保持全新部署的默认值一致

### 5. 启动
```bash
cd ~/code/re_backend/project/docker
cp .env.example .env   # 至少把 ARTALK_TRUSTED_DOMAINS 改成博客的外网地址
docker compose -f docker-compose-server.yml up -d
```
> 首次启动 MySQL 会自动执行 `init-sql/init.sql`（artalk 建库 → 导入 re 主库表结构 + 默认用户/角色/权限/菜单/路由数据），
> 需要几分钟，看 `docker logs re-mysql`。
> 如果你直接把开发机的 `./data/mysql` 整个拷贝过来，初始化会自动跳过。

**默认账号**：
- 后台 `re-admin`：用户名 `admin` / 密码 `adminADMIN+++`（内置超级管理员角色，首次登录后请立即修改）
- 评论系统 artalk：默认无管理员，首次部署后执行以下命令创建（交互式输入用户名/邮箱/密码）：
  ```bash
  docker exec -it re-artalk artalk admin
  ```

### 6. artalk 首次配置（可选）
artalk 控制中心默认站点名等可在 `docker exec -it re-artalk vi /data/artalk.yml` 中调整，
修改后 `docker restart re-artalk` 生效。

### 7. 旧数据图片地址迁移（如果是从开发库拷的数据）
开发时文件 URL 存的是 `http://localhost:30606/...`，公网需要替换：
```sql
UPDATE article SET cover_url = REPLACE(cover_url, 'http://localhost:30606', 'http://re.lingjiatong.cn:30606');
UPDATE article SET markdown_content = REPLACE(markdown_content, 'http://localhost:30606', 'http://re.lingjiatong.cn:30606');
UPDATE article SET html_content = REPLACE(html_content, 'http://localhost:30606', 'http://re.lingjiatong.cn:30606');
```

## 五、验证

```bash
docker compose -f docker-compose-server.yml ps      # 全部 running
curl "http://127.0.0.1:9100/api-frontend/article/list?pageNum=1&pageSize=1"   # 返回 JSON 即通
```

浏览器：外网打开 `http://re.lingjiatong.cn:30151`（博客）、`:30150`（后台）。

## 六、运维

```bash
cd ~/code/re_backend/project/docker
docker compose -f docker-compose-server.yml logs -f re-gateway
docker compose -f docker-compose-server.yml up -d re-gateway   # 重建单个服务（镜像已提前构建好）
```

更新代码：三个仓库 `git pull` → 重新跑 `build-frontend.sh`（或单独 build 变更的镜像）→ `docker compose up -d`。

## 七、安全提醒

- 后台 `re-admin`（30150）建议只对家里 IP 开放路由器转发，或套 Basic Auth
- Nacos 控制台（30603）、Minio 控制台（30607）默认口令较简单，公网暴露的话务必改强密码
- MySQL（30601）、Redis（30602）如非远程调试需要，路由器侧不要转发
