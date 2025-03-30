# Spring Cloud Gateway

## Giới thiệu
Spring Cloud Gateway là một API Gateway mạnh mẽ dành cho các hệ thống microservices, cung cấp các tính năng như routing, filtering, authentication, logging, và rate limiting.

## Công nghệ sử dụng
- **Spring Boot**
- **Spring Cloud Gateway**
- **Spring WebFlux**
- **Eureka (Service Discovery)**
- **Redis (Rate Limiting)**

## 1. Cài đặt
Thêm các dependencies cần thiết vào `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.springframework.boot:spring-boot-starter-redis'
}
```

## 2. Cấu hình API Gateway
Tạo tệp `application.properties` để định tuyến request đến các microservices:

```properties
server.port=8080

spring.cloud.gateway.discovery.locator.enabled=true
spring.cloud.gateway.discovery.locator.lower-case-service-id=true

spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/user/**
spring.cloud.gateway.routes[0].filters[0]=StripPrefix=1

spring.cloud.gateway.routes[1].id=order-service
spring.cloud.gateway.routes[1].uri=lb://order-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/order/**
spring.cloud.gateway.routes[1].filters[0]=StripPrefix=1

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## 3. Thêm Authentication Filter
Tạo một filter để xác thực JWT:

```java
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        if (!request.getHeaders().containsKey("Authorization")) {
            return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
        }
        
        String authHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
        String token = authHeader.replace("Bearer ", "");
        
        if (!validateToken(token)) {
            return onError(exchange, "Invalid Token", HttpStatus.UNAUTHORIZED);
        }
        
        return chain.filter(exchange);
    }

    private boolean validateToken(String token) {
        return token.equals("valid-token"); // Giả lập kiểm tra token
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

## 4. Giới hạn Request (Rate Limiting)
Cấu hình trong `application.properties`:
```properties
spring.cloud.gateway.routes[0].filters[1].name=RequestRateLimiter
spring.cloud.gateway.routes[0].filters[1].args.redis-rate-limiter.replenishRate=5
spring.cloud.gateway.routes[0].filters[1].args.redis-rate-limiter.burstCapacity=10
```

## 5. Chạy thử API Gateway
- Chạy từng microservice:
```sh
./gradlew bootRun --args='--server.port=8081'
./gradlew bootRun --args='--server.port=8082'
```
- Chạy API Gateway:
```sh
./gradlew bootRun --args='--server.port=8080'
```
- Kiểm tra request:
```sh
curl -X GET http://localhost:8080/api/user/test
curl -X GET http://localhost:8080/api/order/test
```

## 6. Kết luận
- ✅ **Spring Cloud Gateway** giúp quản lý API dễ dàng hơn
- ✅ **Eureka** hỗ trợ tự động tìm **microservices**
- ✅ Hỗ trợ **JWT Authentication, Logging, Rate Limiting**

---

📌 **Bạn có thể mở rộng với Circuit Breaker (Resilience4j) hoặc thêm Load Balancer!** 🚀

