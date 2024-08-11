FROM openjdk:21-jdk
EXPOSE 8080
ARG JAR_FILE=build/libs/documentmanagement-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]