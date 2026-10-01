#!/usr/bin/env bash
# 构建 re-frontend / re-admin 前端镜像 + 后端全部镜像
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

echo "==> 构建后端微服务镜像（首次需下载 maven 依赖，较慢）"
cd "$DOCKER_DIR"
docker compose -f docker-compose-server.yml build

echo "==> 全部镜像构建完成："
docker images | grep -E "re-frontend|re-admin|re-gateway|re-auth|re-service|api-backend|api-file" || true
