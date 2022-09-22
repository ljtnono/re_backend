FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
COPY target/api-frontend.jar /
RUN chmod 755 /api-frontend.jar
EXPOSE 8080
CMD ["java", "-jar", "/api-frontend.jar"]