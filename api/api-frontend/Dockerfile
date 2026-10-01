FROM eclipse-temurin:21-jre-alpine
ENV TZ=Asia/Shanghai
MAINTAINER lingjiatong 935188400@qq.com
ENV LANG C.UTF-8
COPY target/api-frontend.jar /
RUN chmod 755 /api-frontend.jar
EXPOSE 9102
CMD ["java", "-Xms512m", "-Xmx512m", "-Duser.timezone=Asia/Shanghai", "-jar", "/api-frontend.jar"]
