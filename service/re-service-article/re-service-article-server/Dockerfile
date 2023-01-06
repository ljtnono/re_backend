FROM ljtnono/java:11
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
COPY target/re-service-article-server.jar /
# 采用上海时区
RUN chmod 755 /re-service-article-server.jar
EXPOSE 8080
CMD ["java","-Xms512m", "-Xmx512m", "-jar", "/re-service-article-server.jar"]
