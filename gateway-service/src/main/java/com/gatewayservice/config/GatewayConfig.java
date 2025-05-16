package com.gatewayservice.config;

import com.gatewayservice.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book-service", r -> r.path("/api/v1/book-service/**")
                        .filters(f -> f
                                .filter(filter)                      // JWT filter của bạn
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                        .setBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5), (int) 2.0, false)
                                )
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter()) // Sử dụng RedisRateLimiter
                                        .setKeyResolver(ipKeyResolver())    // Xác định theo IP
                                )
                                .circuitBreaker(c -> c
                                        .setName("bookCircuitBreaker")
                                        .setFallbackUri("forward:/fallback")
                                )
                        )
                        .uri("lb://BOOK-SERVICE"))
                .route("borrowing-service", r -> r.path("/api/v1/borrowing-service/**")
                        .filters(f -> f
                                .filter(filter)                      // JWT filter của bạn
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                        .setBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5), (int) 2.0, false)
                                )
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter()) // Sử dụng RedisRateLimiter
                                        .setKeyResolver(ipKeyResolver())    // Xác định theo IP
                                )
                                .circuitBreaker(c -> c
                                        .setName("borrowingCircuitBreaker")
                                        .setFallbackUri("forward:/fallback")
                                )
                        ) // Áp dụng filter cho JWT
                        .uri("lb://BORROWING-SERVICE")) // Dịch vụ book-service sử dụng load balancer
                .route("user-service", r -> r.path("/api/v1/user-service/**")
                        .filters(f -> f
                                .filter(filter)                      // JWT filter của bạn
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                        .setBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5), (int) 2.0, false)
                                )
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter()) // Sử dụng RedisRateLimiter
                                        .setKeyResolver(ipKeyResolver())    // Xác định theo IP
                                )
                                .circuitBreaker(c -> c
                                        .setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback"))
                        ) // Áp dụng filter cho JWT
                        .uri("lb://USER-SERVICE")) // Dịch vụ user-service sử dụng load balancer
                .route("inventory-service", r -> r.path("/api/v1/inventory-service/**")
                        .filters(f -> f
                                .filter(filter)                      // JWT filter của bạn
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                        .setBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5), (int) 2.0, false)
                                )
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter()) // Sử dụng RedisRateLimiter
                                        .setKeyResolver(ipKeyResolver())    // Xác định theo IP
                                )
                                .circuitBreaker(c -> c
                                        .setName("inventoryCircuitBreaker")
                                        .setFallbackUri("forward:/fallback"))
                        ) // Áp dụng filter cho JWT
                        .uri("lb://INVENTORY-SERVICE")) // Dịch vụ inventory-service sử dụng load balancer
                .route("notification-service", r -> r.path("/api/v1/notification-service/**")
                        .filters(f -> f
                                .filter(filter)                      // JWT filter của bạn
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                        .setBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5), (int) 2.0, false)
                                )
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter()) // Sử dụng RedisRateLimiter
                                        .setKeyResolver(ipKeyResolver())    // Xác định theo IP
                                )
                                .circuitBreaker(c -> c
                                        .setName("notificationCircuitBreaker")
                                        .setFallbackUri("forward:/fallback"))
                        ) // Áp dụng filter cho JWT
                        .uri("lb://NOTIFICATION-SERVICE")) // Dịch vụ notification-service sử dụng load balancer
                .route("recommendation-service", r -> r.path("/api/v1/recommendation-service/**")
                        .filters(f -> f
                                .filter(filter)                      // JWT filter của bạn
                                .retry(config -> config
                                        .setRetries(3)
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                        .setBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5), (int) 2.0, false)
                                )
                                .requestRateLimiter(c -> c
                                        .setRateLimiter(redisRateLimiter()) // Sử dụng RedisRateLimiter
                                        .setKeyResolver(ipKeyResolver())    // Xác định theo IP
                                )
                                .circuitBreaker(c -> c
                                        .setName("recommendationCircuitBreaker")
                                        .setFallbackUri("forward:/fallback"))
                        )
                        .uri("lb://RECOMMENDATION-SERVICE")) // Dịch vụ notification-service sử dụng load balancer
                .build(); // Kết thúc việc cấu hình các route
    }
    @Bean
    public RedisRateLimiter redisRateLimiter() {
           return new RedisRateLimiter(1, 5);
    }
    @Bean
    KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
