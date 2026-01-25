#------------------------------------------------------------------------------------------------------------------------------------
#Dockerfile for Spring Boot Application including live reload
#------------------------------------------------------------------------------------------------------------------------------------   
#eclipse-temurin -> Image to run java applications
#17-jdk-alpine -> Image containing jdk 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /Backend
#Copy ssl self-signed certificate 
COPY keystore.p12 /Backend
#Copy Spring Boot application.properties file
COPY src/main/resources/application.properties /Backend
#COPY maven files
COPY ["pom.xml", "mvnw", "mvnw.cmd", "/Backend/"]
#Install maven
RUN apk add --no-cache maven 
#Start Spring Boot live reload for development
ENTRYPOINT ["mvn", "spring-boot:run"]