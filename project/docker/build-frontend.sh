#!/usr/bin/env bash
# 一键构建全部镜像（前端 2 个 + 后端 6 个）
# 实际构建逻辑在各仓库根目录的 build.sh 中，这里只做编排
# 前提：re_backend、re_admin、re_frontend 三个仓库克隆在同一目录下
# 用法：bash project/docker/build-frontend.sh
set -e

DOCKER_DIR=$(cd "$(dirname "$0")" && pwd)              # re_backend/project/docker
BACKEND_ROOT=$(cd "$DOCKER_DIR/../.." && pwd)          # re_backend
CODE_DIR=$(cd "$BACKEND_ROOT/.." && pwd)               # 三个仓库的父目录

echo "==> 构建博客前台 re-frontend"
bash "$CODE_DIR/re_frontend/build.sh"

echo "==> 构建管理后台 re-admin"
bash "$CODE_DIR/re_admin/build.sh"

echo "==> 构建后端微服务（首次需下载 maven 依赖，较慢）"
bash "$BACKEND_ROOT/build.sh"

echo "==> 全部镜像构建完成"
