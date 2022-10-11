FROM bitnami/java:11
MAINTAINER lingjiatong 935188400@qq.com
COPY target/re-job.jar /
RUN chmod 755 /re-job.jar
EXPOSE 8080 30086
CMD ["java", "-jar", "/re-job.jar"]