# Discovery Service (Eureka)

## Overview
Discovery Service là một thành phần trong hệ thống microservices của Library System, sử dụng **Spring Cloud Netflix Eureka** để quản lý đăng ký và tra cứu các service. Nó đóng vai trò như một **Service Registry**, cho phép các service khác trong hệ thống (như User Service, Book Service, API Gateway, v.v.) đăng ký và tìm kiếm lẫn nhau.

- **Port mặc định**: `8761`
- **Công nghệ**: Spring Boot, Spring Cloud Netflix Eureka
- **Ticket**: [LIB-101](link-to-ticket)

## Prerequisites
- **Java**: JDK 17 hoặc cao hơn
- **Gradle**: 7.x hoặc cao hơn
- **IDE**: IntelliJ IDEA, VS Code, hoặc bất kỳ IDE nào hỗ trợ Spring Boot (khuyến nghị)
- **Dependencies**: Được quản lý qua `build.gradle`

## Setup Instructions

### 1. Clone Repository
```
git clone https://github.com/your-repo/library-system.git
cd discovery-service
```
### 2. Dependencies
Các dependency chính đã được cấu hình trong `build.gradle`:
```
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
}
```
- Lưu ý: Đảm bảo bạn đã thêm Spring Cloud Dependency Management trong `build.gradle`:
```
plugins {
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

dependencyManagement {
    imports {
        mavenBom 'org.springframework.cloud:spring-cloud-dependencies:2023.0.0'
    }
}
```

### 3. Configuration
Cấu hình nằm trong file `src/main/resources/application.properties`:
```
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.instance.hostname=discovery-service
```
- **Port**: `8761` (có thể thay đổi nếu cần).
- **Eureka Client**: Được tắt để tránh tự đăng ký.
##  Running the Service
### 1. Build Project
```bash
./gradlew clean build
```
### 2. Run Application
- Dùng `Gradle`:
```bash
./gradlew bootRun
```
- Dùng IDE: Mở file DiscoveryServiceApplication.java và chạy trực tiếp.
### 3. Verify
- **Truy cập**: http://localhost:8761
- **Kết quả mong đợi**: Dashboard Eureka hiển thị với giao diện web, chưa có service nào đăng ký (vì các service khác chưa chạy).