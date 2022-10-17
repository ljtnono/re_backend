FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
COPY target/re-service-article-server.jar /
RUN chmod 755 /re-service-article-server.jar
EXPOSE 8080
CMD ["java", "-jar", "/re-service-article-server.jar"]