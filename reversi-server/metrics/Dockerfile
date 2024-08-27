FROM openjdk:21-jdk-alpine
WORKDIR /app
COPY build/libs/metrics.jar /app/metrics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/metrics.jar"]
