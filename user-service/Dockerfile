FROM openjdk:21-jdk-slim
WORKDIR /app
RUN /gradlew clean build -x test
COPY build/libs/user-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]