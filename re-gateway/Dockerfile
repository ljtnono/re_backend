FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
COPY target/re-gateway.jar /
# 采用上海时区
RUN chmod 755 /re-gateway.jar && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8080
CMD ["java","-Xms512m", "-Xmx512m", "-jar", "/re-gateway.jar"]