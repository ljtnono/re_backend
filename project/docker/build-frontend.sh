#!/usr/bin/env bash
# 构建 re-frontend / re-admin 前端镜像 + 后端 6 个微服务镜像
# 前提：re_backend、re_admin、re_frontend 三个仓库克隆在同一目录下
# 用法：bash project/docker/build-frontend.sh
set -e

DOCKER_DIR=$(cd "$(dirname "$0")" && pwd)              # re_backend/project/docker
BACKEND_ROOT=$(cd "$DOCKER_DIR/../.." && pwd)          # re_backend
CODE_DIR=$(cd "$BACKEND_ROOT/.." && pwd)               # 三个仓库的父目录

echo "==> 构建博客前台 re-frontend"
cd "$CODE_DIR/re_frontend"
npm ci
npm run build
docker build -f project/Dockerfile -t re-frontend:latest .

echo "==> 构建管理后台 re-admin"
cd "$CODE_DIR/re_admin"
npm ci
npm run build
docker build -f project/Dockerfile -t re-admin:latest .

echo "==> 构建后端微服务镜像（容器内 mvn package，首次较慢）"
build_backend() {
    local module=$1 jar=$2
    echo "--> $jar"
    docker build --build-arg MODULE="$module" --build-arg JAR_NAME="$jar" \
        -f "$DOCKER_DIR/docker/Dockerfile.backend" -t "$jar:latest" "$BACKEND_ROOT"
}
build_backend re-gateway re-gateway
build_backend re-auth re-auth
build_backend service/re-service-sys/re-service-sys-server re-service-sys-server
build_backend service/re-service-article/re-service-article-server re-service-article-server
build_backend api/api-backend api-backend
build_backend api/api-file api-file

# 后端服务镜像名统一为 compose 中引用的名字（re-service-sys / re-service-article）
docker tag re-service-sys-server:latest re-service-sys:latest
docker tag re-service-article-server:latest re-service-article:latest

echo "==> 全部镜像构建完成："
docker images | grep -E "re-frontend|re-admin|re-gateway|re-auth|re-service|api-backend|api-file" || true
