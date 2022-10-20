FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
ENV JAVA_OPTS = "-Xms512m -Xmx512m"
COPY target/api-frontend.jar /
# 采用上海时区
RUN chmod 755 /api-frontend.jar && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
EXPOSE 8080
CMD ["java", "-jar", "${JAVA_OPTS}", "/api-frontend.jar"]