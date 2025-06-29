FROM openjdk:11-jdk
USER root
VOLUME /tmp
ARG JAR_FILE=target/spring-batch-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
