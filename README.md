# Microservices Project Setup Guide

## 1. Giới thiệu
Dự án này bao gồm các service sau:
- **Discovery Service** (Eureka Server)
- **Gateway Service** (API Gateway)
- **User Service**
- **Book Service**
- **Borrowing Service**
- **Inventory Service**
- **Notification Service**

Các service này giao tiếp với nhau thông qua API Gateway.

---
## 2. Yêu cầu hệ thống
Trước khi chạy project, hãy đảm bảo hệ thống của bạn có:
- **JDK 17+**
- **Gradle 7+**
- **Docker (tuỳ chọn)**
- **Postman hoặc Curl để test API**

---
## 3. Cách chạy các service

### 3.1. Chạy Discovery Service (Eureka Server)
```bash
cd discovery-service
./gradlew bootRun
```
- Eureka Server sẽ chạy trên **http://localhost:8761**
- Kiểm tra trên trình duyệt để đảm bảo service đã khởi động.

### 3.2. Chạy Gateway Service (API Gateway)
```bash
cd gateway-service
./gradlew bootRun
```
- Gateway Service sẽ chạy trên **http://localhost:8080**

### 3.3. Chạy các Service khác
Chạy các service còn lại theo cú pháp:
```bash
cd user-service  # hoặc book-service, borrowing-service, inventory-service, notification-service
./gradlew bootRun
```
Mỗi service sẽ đăng ký với **Eureka Server** và có một cổng riêng:
- **User Service**: `http://localhost:8081`
- **Book Service**: `http://localhost:8082`
- **Borrowing Service**: `http://localhost:8083`
- **Inventory Service**: `http://localhost:8084`
- **Notification Service**: `http://localhost:8085`

---
## 4. Kiểm tra API

### 4.1. Kiểm tra API của từng service
#### User Service
```bash
curl -X GET http://localhost:8081/api/v1/user-service/users
```
#### Book Service
```bash
curl -X GET http://localhost:8082/api/v1/book-service/books
```
#### Borrowing Service
```bash
curl -X GET http://localhost:8083/api/v1/borrowing-service/borrowings
```
#### Inventory Service
```bash
curl -X GET http://localhost:8084/api/v1/inventory-service/inventory
```
#### Notification Service
```bash
curl -X GET http://localhost:8085/api/v1/notification-service/notifications
```

### 4.2. Kiểm tra API qua Gateway
API Gateway sẽ route các request đến các service tương ứng:
```bash
curl -X GET http://localhost:8080/api/v1/user-service/users  # Gửi request đến User Service qua Gateway
curl -X GET http://localhost:8080/api/v1/book-service/books  # Gửi request đến Book Service qua Gateway
curl -X GET http://localhost:8080/api/v1/borrowing-service/borrow-requests  # Gửi request đến Borrowing Service qua Gateway
curl -X GET http://localhost:8080/api/v1/inventory-service/inventory  # Gửi request đến Inventory Service qua Gateway
curl -X GET http://localhost:8080/api/v1/notification-service/notifications  # Gửi request đến Notification Service qua Gateway
```
---
## 5. Kiểm tra đăng ký service trong Eureka
Mở trình duyệt và truy cập **http://localhost:8761**, đảm bảo tất cả service đã đăng ký thành công.

---
## 6. Ghi chú
- Nếu gặp lỗi cổng đã sử dụng, hãy kiểm tra tiến trình đang chạy bằng:
```bash
lsof -i :8080  # Kiểm tra cổng đang sử dụng
kill -9 <PID>  # Dừng tiến trình nếu cần
```
- Kiểm tra log của service khi gặp lỗi:
```bash
tail -f logs/app.log
```

---
## 7. Đóng góp
Nếu bạn gặp lỗi hoặc muốn đóng góp cải tiến, hãy tạo Pull Request hoặc mở Issue trên repository của nhóm.

