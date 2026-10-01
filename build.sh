#!/usr/bin/env bash
# 构建后端全部微服务镜像（多阶段构建：容器内 mvn package，首次较慢）
# 前提：已安装 docker
# 用法：bash build.sh [模块名...]   不带参数构建全部
set -e

ROOT=$(cd "$(dirname "$0")" && pwd)
DOCKER_DIR="$ROOT/project/docker"
DOCKERFILE="$DOCKER_DIR/docker/Dockerfile.backend"

# 模块路径:镜像名 映射
ALL_SERVICES=(
  "re-gateway:re-gateway"
  "re-auth:re-auth"
  "service/re-service-sys/re-service-sys-server:re-service-sys"
  "service/re-service-article/re-service-article-server:re-service-article"
  "api/api-backend:api-backend"
  "api/api-file:api-file"
)

build_one() {
    local module=$1 image=$2
    # jar 名与镜像名一致，除两个 service 模块 finalName 带 -server 后缀
    local jar=$image
    [[ "$image" == re-service-* ]] && jar="${image}-server"
    echo "--> 构建 $image（模块：$module）"
    docker build --build-arg MODULE="$module" --build-arg JAR_NAME="$jar" \
        -f "$DOCKERFILE" -t "$image:latest" "$ROOT"
}

if [ $# -eq 0 ]; then
    for pair in "${ALL_SERVICES[@]}"; do
        build_one "${pair%%:*}" "${pair##*:}"
    done
else
    for want in "$@"; do
        found=0
        for pair in "${ALL_SERVICES[@]}"; do
            [ "$want" = "${pair%%:*}" -o "$want" = "${pair##*:}" ] && { build_one "${pair%%:*}" "${pair##*:}"; found=1; break; }
        done
        [ $found -eq 0 ] && echo "!! 未知服务：$want（可选：$(echo "${ALL_SERVICES[@]}" | tr ' ' '\n' | cut -d: -f2 | tr '\n' ' ')）" && exit 1
    done
fi

echo "==> 全部后端镜像构建完成"
docker images | grep -E "re-gateway|re-auth|re-service|api-backend|api-file" || true
