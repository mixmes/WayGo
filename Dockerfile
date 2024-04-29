FROM openjdk:17-oracle
COPY server1.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","server1.jar"]