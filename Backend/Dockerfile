#------------------------------------------------------------------------------------------------------------------------------------
#Dockerfile for Spring Boot Application
#------------------------------------------------------------------------------------------------------------------------------------   
#eclipse-temurin -> Image to run java applications
#17-jre-alpine -> Light weight and Application runs on Java Version 17, same as spring application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
#.env file contains values for database connection
COPY *.env /app/.env
#Spring Boot application.properties file
COPY /src/main/resources/application.properties /app/application.properties
#jar file holding application
COPY /build/libs/forum-0.0.1-SNAPSHOT.jar /app/forum-0.0.1-SNAPSHOT.jar
#Start jar
ENTRYPOINT ["java","-jar","forum-0.0.1-SNAPSHOT.jar"]