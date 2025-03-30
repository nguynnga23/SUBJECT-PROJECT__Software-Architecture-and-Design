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
