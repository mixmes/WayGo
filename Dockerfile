FROM openjdk:17-oracle
ARG JAR_FILE
COPY ${JAR_FILE} .
EXPOSE 8080
ENTRYPOINT ["java","-jar","server1.jar"]