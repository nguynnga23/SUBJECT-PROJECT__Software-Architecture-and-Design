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
- **Recommendation Service**

Các service này giao tiếp với nhau thông qua API Gateway.

---
## 2. Yêu cầu hệ thống
Trước khi chạy project, hãy đảm bảo hệ thống của bạn có:
- **JDK 17+**
- **Gradle 7+**
- **Docker (tuỳ chọn)**
- **Postman hoặc Curl để test API**

## 3. Cách chạy các service
### 3.1. Chạy project với Docker Compose

#### Cài đặt Docker Compose
Đảm bảo bạn đã cài đặt Docker và Docker Compose trên hệ thống của mình.

#### Chạy các service
Chạy lệnh sau trong thư mục gốc của dự án:
```bash
docker-compose up --build
```

#### Kiểm tra các container
Sau khi chạy, kiểm tra các container đang hoạt động:
```bash
docker ps
```

#### Dừng các container
Để dừng các container, sử dụng lệnh:
```bash
docker-compose down
```

#### Truy cập các service
- **Eureka Server**: [http://localhost:8761](http://localhost:8761)
- **API Gateway**: [http://localhost:8080](http://localhost:8080)
- **User Service**: [http://localhost:8081](http://localhost:8081)
- **Book Service**: [http://localhost:8082](http://localhost:8082)
- **Borrowing Service**: [http://localhost:8083](http://localhost:8083)
- **Inventory Service**: [http://localhost:8084](http://localhost:8084)
- **PgAdmin**: [http://localhost:80](http://localhost:80)
- **Mongo Express**: [http://localhost:9090](http://localhost:9090)

#### Ghi chú
| Lệnh                              | Đặc điểm                                                                 |
| --------------------------------- | ------------------------------------------------------------------------ |
| `docker-compose up`               | Chỉ khởi động container, không build lại image nếu đã có sẵn.            |
| `docker-compose build`            | Chỉ build image, không khởi động container.                              |
| `docker-compose up --build`       | Build lại image (nếu cần) và khởi động container.                        |
| `docker-compose build --no-cache` | Build lại image **mà không dùng cache**, đảm bảo tất cả các lớp đều mới. |

- Đảm bảo các biến môi trường trong file `.env` được cấu hình đúng.
- Nếu gặp lỗi, kiểm tra log bằng lệnh:
```bash
docker-compose logs -f
```

#### Rebuild Docker images without cache
Nếu bạn muốn xây dựng lại toàn bộ các image Docker mà không sử dụng cache, hãy chạy lệnh sau:
```bash
docker-compose build --no-cache
```
Sau đó, khởi động lại các container:
```bash
docker-compose up -d
```
### 3.2. Chạy Discovery Service (Eureka Server)
```bash
cd discovery-service
./gradlew bootRun
```
- Eureka Server sẽ chạy trên **http://localhost:8761**
- Kiểm tra trên trình duyệt để đảm bảo service đã khởi động.

### 3.3. Chạy Gateway Service (API Gateway)
```bash
cd gateway-service
./gradlew bootRun
```
- Gateway Service sẽ chạy trên **http://localhost:8080**

### 3.4. Chạy các Service khác
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
- **Recommendation Service**: `http://localhost:8086`
---
## 4. Kiểm tra API

### Swagger UI URL
- **User Service**:  http://localhost:8081/swagger-ui/index.html
- **Book Service**: http://localhost:8082/swagger-ui/index.html
- **Borrowing Service**: http://localhost:8083/swagger-ui/index.html
- **Inventory Service**: http://localhost:8084/swagger-ui/index.html
- **Notification Service**: http://localhost:8085/swagger-ui/index.html
- **Recommendation Service**: http://localhost:8086/swagger-ui/index.html

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

#### Recommendation Service
```bash
curl -X GET http://localhost:8086/api/v1/recommendation-service/recommendations
```

### 4.2. Kiểm tra API qua Gateway
API Gateway sẽ route các request đến các service tương ứng:
```bash
curl -X GET http://localhost:8080/api/v1/user-service/users  # Gửi request đến User Service qua Gateway
curl -X GET http://localhost:8080/api/v1/book-service/books  # Gửi request đến Book Service qua Gateway
curl -X GET http://localhost:8080/api/v1/borrowing-service/borrow-requests  # Gửi request đến Borrowing Service qua Gateway
curl -X GET http://localhost:8080/api/v1/inventory-service/inventory  # Gửi request đến Inventory Service qua Gateway
curl -X GET http://localhost:8080/api/v1/notification-service/notifications  # Gửi request đến Notification Service qua Gateway
curl -X GET http://localhost:8080/api/v1/recommendation-service  # Gửi request đến Notification Service qua Gateway

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

---


