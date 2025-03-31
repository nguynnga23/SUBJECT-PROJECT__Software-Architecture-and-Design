---
title: Sequence Diagram
---

## 1. JWT processing process in Spring Cloud Gateway

```plantuml
@startuml
actor Client
participant "Gateway" as Gateway
participant "JwtFilter" as JwtFilter
participant "JwtUtil" as JwtUtil
participant "NextFilter" as NextFilter

Client -> Gateway: Gửi HTTP Request
Gateway -> JwtFilter: filter(exchange, chain)

alt Endpoint không cần bảo mật (/register, /login)
    JwtFilter -> NextFilter: chain.filter(exchange)
    NextFilter -> Gateway: Tiếp tục xử lý
    Gateway -> Client: HTTP Response
else Endpoint cần bảo mật
    JwtFilter -> JwtFilter: Kiểm tra header "Authorization"
    alt Header "Authorization" không tồn tại
        JwtFilter -> Gateway: setStatusCode(401 Unauthorized)
        Gateway -> Client: HTTP 401 Unauthorized
    else Header "Authorization" tồn tại
        JwtFilter -> JwtUtil: validateToken(token)
        alt Token không hợp lệ
            JwtUtil -> JwtFilter: Throw JwtTokenMalformedException\nhoặc JwtTokenMissingException
            JwtFilter -> Gateway: setStatusCode(400 Bad Request)
            Gateway -> Client: HTTP 400 Bad Request
        else Token hợp lệ
            JwtFilter -> JwtUtil: getClaims(token)
            JwtUtil -> JwtFilter: Trả về Claims
            JwtFilter -> JwtFilter: Thêm "id" vào header
            JwtFilter -> NextFilter: chain.filter(exchange với header mới)
            NextFilter -> Gateway: Tiếp tục xử lý
            Gateway -> Client: HTTP Response
        end
    end
end

@enduml
```


**1. `Client` gửi request:** `Client` gửi một HTTP request tới `Gateway` (Spring Cloud Gateway).

**2. `Gateway` gọi `JwtFilter`:** Gateway áp dụng `JwtAuthenticationFilter` bằng cách gọi phương thức `filter(exchange, chain)`.

**3. Kiểm tra endpoint:**
- Nếu endpoint không cần bảo mật (ví dụ: /register, /login), `JwtFilter` gọi `chain.filter(exchange)` để chuyển tiếp request tới `NextFilter`.
- Nếu endpoint cần bảo mật, `JwtFilter` kiểm tra header `Authorization`.

**4. Xử lý header Authorization:**
- Nếu header không tồn tại, `JwtFilter` trả về mã trạng thái 401 Unauthorized thông qua `Gateway`.
- Nếu header tồn tại, `JwtFilter` gọi `JwtUtil.validateToken(token)` để kiểm tra tính hợp lệ của token.

**5. Xác thực token:**
- Nếu token không hợp lệ, `JwtUtil` ném ngoại lệ, và `JwtFilter` trả về mã trạng thái 400 Bad Request.
- Nếu token hợp lệ, `JwtFilter` gọi `JwtUtil.getClaims(token)` để lấy thông tin `Claims`, thêm id vào header, rồi chuyển tiếp request tới `NextFilter`.

**6. Hoàn tất xử lý:** `NextFilter` tiếp tục xử lý request, và `Gateway` trả về response cho `Client`.


## 2. JWT processing process in Spring Cloud Gateway and User Service

```plantuml
@startuml
actor Client
participant "API Gateway" as Gateway
participant "JwtFilter" as JwtFilter
participant "User Service" as UserService

== Đăng nhập ==
Client -> Gateway: POST /api/user/login\n{username, password}
Gateway -> JwtFilter: filter(exchange)
JwtFilter -> JwtFilter: Endpoint /login không cần xác thực
JwtFilter -> UserService: Chuyển tiếp request
UserService -> UserService: Xác thực username/password
UserService -> UserService: Tạo JWT token
UserService -> Gateway: {token: "eyJhbGciOiJIUzI1NiJ9..."}
Gateway -> Client: {token: "eyJhbGciOiJIUzI1NiJ9..."}

== Truy cập profile ==
Client -> Gateway: GET /api/user/profile\nAuthorization: Bearer <token>
Gateway -> JwtFilter: filter(exchange)
JwtFilter -> JwtFilter: Endpoint /profile cần xác thực
JwtFilter -> JwtFilter: Lấy token từ header
JwtFilter -> UserService: validateToken(token)
alt Token hợp lệ
    JwtFilter -> UserService: getClaims(token)
    JwtFilter -> JwtFilter: Thêm "id" vào header
    JwtFilter -> UserService: Chuyển tiếp request với header "id"
    UserService -> UserService: Lấy profile bằng userId
    UserService -> Gateway: {username, email}
    Gateway -> Client: {username, email}
else Token không hợp lệ
    JwtFilter -> Gateway: HTTP 400 Bad Request
    Gateway -> Client: HTTP 400
end

@enduml
```
## 3. Register, Authentication, Login, Logout
```plantuml
@startuml
autonumber

actor User
participant Frontend as "Frontend (React/Angular/Vue)"
participant Backend as "Backend (Spring Boot)"
database Database as "PostgreSQL"
participant Redis as "Redis (Optional)"

== Register Flow ==
User -> Frontend: Enter registration details
Frontend -> Backend: POST /api/users/register\n(username, email, password)
Backend -> Backend: Validate input (Spring Validation)
Backend -> Backend: Hash password (BCrypt)
Backend -> Database: Save user to PostgreSQL
Database --> Backend: Success
Backend --> Frontend: 201 Created

== Login Flow ==
User -> Frontend: Enter login credentials
Frontend -> Backend: POST /api/users/login\n(username, password)
Backend -> Database: Fetch user by username/email
Database --> Backend: User found
Backend -> Backend: Compare password (BCrypt)
alt Password matches
    Backend -> Backend: Generate accessToken (JWT)\nGenerate refreshToken (JWT)
    Backend -> Frontend: Return accessToken\nSet refreshToken in HttpOnly cookie
else Password does not match
    Backend --> Frontend: 401 Unauthorized
end

== Authentication Flow ==
User -> Frontend: Access protected resource
Frontend -> Backend: GET /api/protected-resource\nAuthorization: Bearer <accessToken>
Backend -> Backend: Validate accessToken (JWT)\n(Spring Security)
alt Token valid
    Backend -> Database: (Optional) Fetch user details
    Database --> Backend: User details
    Backend --> Frontend: Return resource data
else Token invalid
    Backend --> Frontend: 401 Unauthorized
end

== Refresh Token Flow ==
User -> Frontend: Token expired
Frontend -> Backend: POST /api/users/refresh-token\n(refreshToken in HttpOnly cookie)
Backend -> Backend: Validate refreshToken (JWT)
alt Refresh token valid
    Backend -> Backend: Generate new accessToken (JWT)
    Backend -> Frontend: Return new accessToken
else Refresh token invalid
    Backend --> Frontend: 401 Unauthorized
end

== Logout Flow ==
User -> Frontend: Click logout
Frontend -> Backend: POST /api/users/logout\nAuthorization: Bearer <accessToken>
Backend -> Redis: Add accessToken to blacklist (Optional)
Backend -> Frontend: Clear refreshToken cookie\nReturn success

@enduml
```
### Flow compares Session-based (default Spring Security) and JWT + Stateless
```plantuml
@startuml
actor Client
participant "Server (Session-based)" as SessionServer
participant "Server (JWT + Stateless)" as JwtServer
participant "Database" as DB

== Session-based Login ==
Client -> SessionServer: POST /login\n(username, password)
SessionServer -> DB: Query user
DB --> SessionServer: User data
SessionServer -> SessionServer: Validate (AuthenticationManager)
SessionServer -> SessionServer: Create session (JSESSIONID)
SessionServer --> Client: 200 OK\nSet-Cookie: JSESSIONID

== JWT + Stateless Login ==
Client -> JwtServer: POST /login\n(username, password)
JwtServer -> DB: Query user
DB --> JwtServer: User data
JwtServer -> JwtServer: Validate (AuthenticationService)
JwtServer -> JwtServer: Generate JWT (accessToken, refreshToken)
JwtServer --> Client: 200 OK\n{accessToken}, Cookie: refreshToken

== Access Protected Resource ==
Client -> SessionServer: GET /protected\nCookie: JSESSIONID
SessionServer -> SessionServer: Check session
SessionServer --> Client: 200 OK\nData

Client -> JwtServer: GET /protected\nAuthorization: Bearer <accessToken>
JwtServer -> JwtServer: Validate token (JwtTokenFilter)
JwtServer --> Client: 200 OK\nData

@enduml
```

### Stream with authentication and authorization
```plantuml
@startuml
actor Client
participant "API Gateway" as Gateway
participant "User Service" as UserService
participant "Database (PostgreSQL)" as DB
participant "Redis" as Cache

== Register ==
Client -> Gateway: POST /register\n(username, password, email, ...)
Gateway -> UserService: Forward request
UserService -> DB: Save new user
DB --> UserService: User saved
UserService --> Gateway: 201 Created\n{userId}
Gateway --> Client: 201 Created\n{userId}

== Login ==
Client -> Gateway: POST /login\n(username, password)
Gateway -> UserService: Forward request
UserService -> DB: Query user
DB --> UserService: User data
UserService -> UserService: Validate password\nGenerate JWT (userId, role)
UserService -> Cache: Store refreshToken
Cache --> UserService: OK
UserService --> Gateway: 200 OK\n{accessToken}, Cookie: refreshToken
Gateway --> Client: 200 OK\n{accessToken}, Cookie: refreshToken

== Update Profile ==
Client -> Gateway: PUT /users/{userId}\nAuthorization: Bearer <accessToken>\n(fullName, ...)
Gateway -> Gateway: Validate token\nExtract userId, role
Gateway -> UserService: Forward request
UserService -> UserService: Check role (USER or ADMIN)\nCheck userId matches or ADMIN
UserService -> DB: getUserById(userId)
DB --> UserService: User data
UserService -> DB: updateUser(userId, updatedData)
DB --> UserService: Updated user
UserService --> Gateway: 200 OK\nUpdated user
Gateway --> Client: 200 OK\nUpdated user

@enduml
```
## 4. Student create a book borrowing request

``` plantuml
@startuml
actor Student
participant "API Gateway" as Gateway
participant "JwtFilter" as JwtFilter
participant "User Service" as UserService
participant "Borrow Service" as BorrowService

== Đăng nhập ==
Student -> Gateway: POST /api/user/login\n{studentCode, password}
Gateway -> JwtFilter: filter(exchange)
JwtFilter -> JwtFilter: /login không cần xác thực
JwtFilter -> UserService: Chuyển tiếp request
UserService -> UserService: Xác thực sinh viên
UserService -> UserService: Tạo JWT token\n(chứa studentId)
UserService -> Gateway: {token}
Gateway -> Student: {token}

== Tạo phiếu mượn sách ==
Student -> Gateway: POST /api/borrow/create\nAuthorization: Bearer <token>\n{bookId}
Gateway -> JwtFilter: filter(exchange)
JwtFilter -> JwtFilter: /create cần xác thực
JwtFilter -> JwtFilter: Lấy token từ header
JwtFilter -> UserService: validateToken(token)
alt Token hợp lệ
    JwtFilter -> UserService: getClaims(token)
    JwtFilter -> JwtFilter: Thêm "studentId" vào header
    JwtFilter -> BorrowService: Chuyển tiếp request
    BorrowService -> BorrowService: Tạo phiếu mượn sách
    BorrowService -> Gateway: {borrowSlip}
    Gateway -> Student: {borrowSlip}
else Token không hợp lệ
    JwtFilter -> Gateway: HTTP 400
    Gateway -> Student: HTTP 400
end

@enduml
```