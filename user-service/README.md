# Spring Boot User API

### Prerequisites

- Java (JDK 17+)
- Gradle
- PostgreSQL/pgAdmin4 or container PostgreSQL/PgAdmin4 by Docker
- IDE (IntelliJ IDEA, Eclipse, hoặc bất kỳ IDE nào hỗ trợ Java)
- Postman or ThunderClient for VSCode
- Redis

### PostgreSQL for docker
- Step 1: Run container PostgreSQL by command:
```bash
docker-compose up -d
```
- Acceptable results:
![img_8.png](img_8.png)
- Step 2: Access the path: Localhost:80 or Localhost and login with auth of UI pgAdmin4 before:
![img_1.png](img_1.png)
- Step 3: Register server for UI: 
![img_2.png](img_2.png)
- Step 4: Enter Name
![img_3.png](img_3.png)
- Step 5: Enter username and password are 'root'
![img_4.png](img_4.png)
- Step 6: Value of Host name/address is IPAddress of postgres-container in Docker. Enter this command to see IPAddress of postgres-container:
```bash
docker inspect postgres-container
```
- Result and save: ![img_5.png](img_5.png) ![img_6.png](img_6.png)
- Step 7: Open console redis by command:
```bash
redis-cli
```
- Result:
![img_9.png](img_9.png)
### Start project user-service
- Step 1: (If any) Create empty table users in DB user-service
- Step 2: Start project user-service
- Step 3: Test postman or ThunderClient

***Dinh Nguyen Chung***

**Register:**
```angular2html
http://localhost:8080/api/users/register
```
![img_13.png](img_13.png)
#### Register Thất bại  (trùng userName)
![img_20.png](img_20.png)
**Login**
```angular2html
http://localhost:8080/api/users/login
```
![img_26.png](img_26.png)
Thành công Login sẽ trả về accessToken và accessToken có time là 30', refreshToken có thời gian 7 ngày được lưu trên cookies.

![img_15.png](img_15.png)

Login thất bại 

![img_25.png](img_25.png)

### Khi thực hiện thực hiện refreshToken 


Chọn phương thức POST.

Nhập URL:
```angular2html
http://localhost:8080/api/users/refresh-token
```
Sẽ tự lấy refreshToken trên cookies để lấy access Token mới
![img_27.png](img_27.png)

### Kiểm tra tính hợp lệ của access token mới va Get User

Lưu lại accessToken mới từ response (nếu đã refreshToken ) để thực hiện

Method: GET

URL: 
```
http://localhost:8080/api/users/profile/{userId}
```

Thay {userId} 

Header:

Authorization: Bearer new_access_token

![img_28.png](img_28.png)


### Logout
Method : POST

URL:
```angular2html
http://localhost:8080/api/users/logout
```

Lấy accessToken gửi vào Beer Token

![img_18.png](img_18.png)

### Kiểm tra accessToken ()
```
http://localhost:8080/api/users/protected-api
```

![img_23.png](img_23.png)

Logout Thành công

![img_24.png](img_24.png)

### Update User check token

PUT

```
http://localhost:8080/api/users/{Id}
```

![img_29.png](img_29.png)

Thieu hoac sai Token 

![img_30.png](img_30.png)