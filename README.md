# Microservices Project Setup Guide

## 1. Giới thiệu
Dự án này bao gồm các service sau:
- **Config Service** (Eureka Server)
- **Discovery Service** (Eureka Server)
- **Gateway Service** (API Gateway)
- **User Service**
- **Book Service**
- **Borrowing Service**
- **Inventory Service**
- **Notification Service**

Các service này giao tiếp với nhau thông qua API Gateway.

## 2. Yêu cầu hệ thống
Trước khi chạy project, hãy đảm bảo hệ thống của bạn có:
- **JDK 17+**
- **Gradle 7+**
- **Docker (tuỳ chọn)**
- **Postman hoặc Curl để test API**

### Hướng dẫn tạo file `.env` từ `.env.example`
1. Trong thư mục gốc dự án, sao chép file `.env.example` thành `.env`:
    ```bash
    cp .env.example .env
    ```
2. Mở file `.env` vừa tạo và điền các thông tin cấu hình phù hợp với môi trường của bạn (database, email, ...).

### Hướng dẫn tạo Email và Password cho Spring Email

Để sử dụng chức năng gửi email trong Spring (ví dụ cho Notification Service), bạn cần có tài khoản email và mật khẩu ứng dụng (App Password):

### Bước 1: Tạo tài khoản Gmail (nếu chưa có)
- Đăng ký tại https://mail.google.com/

### Bước 2: Bật xác minh 2 bước cho Gmail
- Truy cập https://myaccount.google.com/security
- Bật xác minh 2 bước (2-Step Verification).

### Bước 3: Tạo App Password (Mật khẩu ứng dụng)
- Sau khi bật xác minh 2 bước, vào mục "App passwords" (Mật khẩu ứng dụng).
- Chọn "Mail" cho ứng dụng và "Other" (hoặc chọn thiết bị tuỳ ý).
- Google sẽ tạo cho bạn một mật khẩu ứng dụng gồm 16 ký tự.

### Bước 4: Thêm thông tin vào file `.env`
Thêm các dòng sau vào file `.env` ở thư mục gốc dự án:
```
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```
**Lưu ý:** Không chia sẻ mật khẩu ứng dụng công khai và tuyệt đối không Commit và Push lên Repo Github.
## 3. Cách chạy các service sử dụng Docker
### 3.1. Chạy project với Docker Compose

#### Cài đặt Docker Compose
Đảm bảo bạn đã cài đặt Docker và Docker Compose trên hệ thống của mình.

#### Chạy các service
Chạy lệnh sau trong thư mục gốc của dự án:
```bash
docker-compose build --no-cache
```
```bash
docker-compose up -d
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

Mỗi service sẽ đăng ký với **Eureka Server** và có một cổng riêng:
- **User Service**: `http://localhost:8081`
- **Book Service**: `http://localhost:8082`
- **Borrowing Service**: `http://localhost:8083`
- **Inventory Service**: `http://localhost:8084`
- **Notification Service**: `http://localhost:8085`
## 4. Kiểm tra đăng ký service trong Eureka
Mở trình duyệt và truy cập **http://localhost:8761**, đảm bảo tất cả service đã đăng ký thành công.

## 5. Kiểm tra API

### 5.1. Kiểm tra API của từng Service với Swagger UI URL
- **User Service**:  http://localhost:8081/swagger-ui/index.html
- **Book Service**: http://localhost:8082/swagger-ui/index.html
- **Borrowing Service**: http://localhost:8083/swagger-ui/index.html
- **Inventory Service**: http://localhost:8084/swagger-ui/index.html
- **Notification Service**: http://localhost:8085/swagger-ui/index.html

### 5.2. Kiểm tra API qua Gateway

API Gateway sẽ route các request đến các service tương ứng:
```bash
curl -X GET http://localhost:8080/api/v1/user-service/users  # Gửi request đến User Service qua Gateway
curl -X GET http://localhost:8080/api/v1/book-service/books  # Gửi request đến Book Service qua Gateway
curl -X GET http://localhost:8080/api/v1/borrowing-service/borrow-requests  # Gửi request đến Borrowing Service qua Gateway
curl -X GET http://localhost:8080/api/v1/inventory-service/inventory  # Gửi request đến Inventory Service qua Gateway
curl -X GET http://localhost:8080/api/v1/notification-service/notifications  # Gửi request đến Notification Service qua Gateway

```
## 6. Tài liệu liên quan
- `https://nguynnga23.github.io/SUBJECT-PROJECT__Software-Architecture-and-Design/docs/intro`
- `https://telling-tray-627.notion.site/K-ch-b-n-Thuy-t-tr-nh-v-Demo-20-ph-t-1f644365a56880428d54cccc33219764`
- `https://xmind.ai/share/TllsZOeX`




