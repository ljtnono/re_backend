FROM java:8
MAINTAINER lingjiatong 935188400@qq.com
COPY target/re-service-sys-server.jar /
RUN chmod 755 /re-service-sys-server.jar
EXPOSE 8080
CMD ["java", "-jar", "/re-service-sys-server.jar"]