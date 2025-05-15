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
# Cài dockerize
ENV DOCKERIZE_VERSION v0.7.0
RUN apt-get update && apt-get install -y curl && \
    curl -L https://github.com/jwilder/dockerize/releases/download/${DOCKERIZE_VERSION}/dockerize-linux-amd64-${DOCKERIZE_VERSION}.tar.gz \
    | tar -C /usr/local/bin -xz && \
    apt-get remove -y curl && apt-get autoremove -y && apt-get clean

COPY --from=builder /app/build/libs/gateway-service-*-SNAPSHOT.jar app.jar
# Sao chép thư mục resources vào image
COPY --from=builder /app/src/main/resources /app/resources
ENTRYPOINT ["dockerize", "-wait", "tcp://config-service:8888","-wait", "tcp://discovery-service:8761", "-timeout", "60s", "java", "-jar", "app.jar"]



