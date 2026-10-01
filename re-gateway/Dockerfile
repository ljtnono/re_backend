FROM amazoncorretto:21-alpine
ENV TZ=Asia/Shanghai
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
COPY target/re-gateway.jar /
# 采用上海时区
RUN chmod 755 /re-gateway.jar
EXPOSE 8080
CMD ["java", "-Xms512m", "-Xmx512m", "-Duser.timezone=Asia/Shanghai", "-jar", "/re-gateway.jar"]
