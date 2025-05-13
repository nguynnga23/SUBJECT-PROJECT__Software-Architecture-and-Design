# Stage 1: Build ứng dụng
FROM gradle:8-jdk21-alpine AS builder

WORKDIR /app

# Copy wrapper và file cấu hình (để chỉ build dependencies trước)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build thật sự
RUN ./gradlew clean build -x test

# Stage 2: Chạy ứng dụng
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/gateway-service-*-SNAPSHOT.jar app.jar

# Sao chép thư mục resources vào image
COPY --from=builder /app/src/main/resources /app/resources

ENTRYPOINT ["java", "-jar", "app.jar"]


