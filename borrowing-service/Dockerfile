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
# Cài dockerize
# ENV DOCKERIZE_VERSION v0.7.0
# RUN apt-get update && apt-get install -y curl && \
#     curl -L https://github.com/jwilder/dockerize/releases/download/${DOCKERIZE_VERSION}/dockerize-linux-amd64-${DOCKERIZE_VERSION}.tar.gz \
#     | tar -C /usr/local/bin -xz && \
#     apt-get remove -y curl && apt-get autoremove -y && apt-get clean

COPY --from=builder /app/build/libs/borrowing-service-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
