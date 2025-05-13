# Stage 1: Build ứng dụng
FROM gradle:8-jdk21-alpine AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

# Không dùng wrapper nữa
RUN gradle clean build -x test

# Stage 2: Run app
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/borrowing-service-*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]