FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
ENV JAVA_OPTS = "-Xms512m -Xmx512m"
COPY target/re-service-article-server.jar /
# 采用上海时区
RUN chmod 755 /re-service-article-server.jar && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8080
CMD ["java", "${JAVA_OPTS}", "-jar", "/re-service-article-server.jar"]