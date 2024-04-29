FROM openjdk:17
ARG JAR_FILE
COPY ${JAR_FILE} .
EXPOSE 8080
ENTRYPOINT ["java","-jar","server1.jar"]