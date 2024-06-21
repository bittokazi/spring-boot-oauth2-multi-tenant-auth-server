FROM maven:3.8.5-openjdk-17 AS MAVEN_TOOL_CHAIN
LABEL maintainer=bitto.kazi@gmail.com
COPY ./ /tmp/app
WORKDIR /tmp/app
RUN mvn clean package
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=MAVEN_TOOL_CHAIN /tmp/app/target/spring-boot-oauth2-auth-server-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]