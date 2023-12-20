FROM openjdk:17-jdk-alpine
MAINTAINER Ricky
COPY target/*.jar Library_App.jar
ENTRYPOINT ["java","-jar","/Library_app.jar"]