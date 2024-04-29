FROM openjdk:17-oracle
COPY server.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]