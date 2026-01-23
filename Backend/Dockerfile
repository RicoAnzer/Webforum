#------------------------------------------------------------------------------------------------------------------------------------
#Dockerfile for Spring Boot Application
#------------------------------------------------------------------------------------------------------------------------------------   
#eclipse-temurin -> Image to run java applications
#17-jre-alpine -> Light weight and Application runs on Java Version 17, same as spring application
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /Backend
#Copy ssl self-signed certificate 
COPY *keystore.p12 /Backend/keystore.p12
#Copy Spring Boot application.properties file
COPY /src/main/resources/application.properties /Backend/application.properties
#Copy gradle files
COPY gradlew /Backend/gradlew
COPY build.gradle /Backend/build.gradle
COPY settings.gradle /Backend/settings.gradle
COPY ./gradle /Backend/gradle
COPY ./.gradle /Backend/.gradle
#Start Spring Boot hot reload
ENTRYPOINT ["./gradlew", "bootRun", "--continuous"]