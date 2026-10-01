#!/usr/bin/env bash
# 构建后端全部微服务镜像
# 流程：mvn package 打 jar -> 使用各服务自己的 Dockerfile 构建镜像
# 前提：已安装 JDK21、maven、docker
# 用法：bash build.sh [服务名...]   不带参数构建全部
#   服务名可用模块路径（api/api-file）或镜像名（api-file）
set -e

ROOT=$(cd "$(dirname "$0")" && pwd)
cd "$ROOT"

# 模块路径:镜像名 映射（镜像名需与 docker-compose-server.yml 中一致）
ALL_SERVICES=(
  "re-gateway:re-gateway"
  "re-auth:re-auth"
  "service/re-service-sys/re-service-sys-server:re-service-sys"
  "service/re-service-article/re-service-article-server:re-service-article"
  "api/api-backend:api-backend"
  "api/api-file:api-file"
  "api/api-frontend:api-frontend"
)

# 解析参数 -> 需要构建的 模块:镜像 列表
TARGETS=()
if [ $# -eq 0 ]; then
    TARGETS=("${ALL_SERVICES[@]}")
else
    for want in "$@"; do
        found=0
        for pair in "${ALL_SERVICES[@]}"; do
            if [ "$want" = "${pair%%:*}" ] || [ "$want" = "${pair##*:}" ]; then
                TARGETS+=("$pair"); found=1; break
            fi
        done
        if [ $found -eq 0 ]; then
            echo "!! 未知服务：$want（可选：$(echo "${ALL_SERVICES[@]}" | tr ' ' '\n' | cut -d: -f2 | tr '\n' ' ')）"
            exit 1
        fi
    done
fi

# 第一步：mvn 打包（一次命令把所有需要的模块都打了）
MODULES=$(echo "${TARGETS[@]}" | tr ' ' '\n' | cut -d: -f1 | sort -u | sed 's/[^ ]*/-pl &/' | tr '\n' ' ')
echo "==> mvn package ${MODULES}"
mvn -B -ntp ${MODULES} -am package -DskipTests -P prod -Dnacos-server-addr=re-nacos:8848

# 第二步：逐个用服务自己的 Dockerfile 构建镜像（构建上下文 = 模块目录）
for pair in "${TARGETS[@]}"; do
    module=${pair%%:*}; image=${pair##*:}
    echo "==> docker build $image（$module/Dockerfile）"
    docker build -t "$image:latest" "$ROOT/$module"
done

echo "==> 全部后端镜像构建完成"
docker images | grep -E "re-gateway|re-auth|re-service|api-backend|api-file" || true
