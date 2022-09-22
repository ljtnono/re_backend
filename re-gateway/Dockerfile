FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
COPY target/re-gateway.jar /
RUN chmod 755 /re-gateway.jar
EXPOSE 8080
CMD ["java", "-jar", "/re-gateway.jar"]