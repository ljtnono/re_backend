FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
COPY target/re-auth.jar /
COPY SIMYOU.TTF /
# 采用上海时区
RUN chmod 755 /re-auth.jar && \
    echo "deb https://mirrors.tuna.tsinghua.edu.cn/debian/ bullseye main contrib non-free" >> /etc/apt/sources.list && \
    echo "deb https://mirrors.tuna.tsinghua.edu.cn/debian/ bullseye-updates main contrib non-free" >> /etc/apt/sources.list && \
    echo "deb https://mirrors.tuna.tsinghua.edu.cn/debian/ bullseye-backports main contrib non-free" >> /etc/apt/sources.list && \
    echo "deb https://mirrors.tuna.tsinghua.edu.cn/debian-security bullseye-security main contrib non-free" >> /etc/apt/sources.list && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone && \
    apt-get update && \
    apt-get install fontconfig -y && \
    # set fonts
    mkdir -p /usr/share/fonts/chinese && \
    mv /SIMYOU.TTF /usr/share/fonts/chinese && \
    chmod -R 755 /usr/share/fonts/chinese && \
    fc-cache
EXPOSE 8080
CMD ["java","-Xms512m", "-Xmx512m", "-jar", "/re-auth.jar"]
