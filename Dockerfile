FROM openjdk:17-oracle
COPY server.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]