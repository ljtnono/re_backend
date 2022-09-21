FROM java:8
MAINTAINER lingjiatong 935188400@qq.com
COPY target/api-re-frontend.jar /
RUN chmod 755 /api-re-frontend.jar
EXPOSE 8080
CMD ["java", "-jar", "/api-re-frontend.jar"]