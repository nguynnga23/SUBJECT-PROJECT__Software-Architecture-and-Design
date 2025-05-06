package com.gatewayservice.config;

import com.gatewayservice.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book-service", r -> r.path("/api/v1/book-service/**")
                        .filters(f -> f.filter(filter)) // Áp dụng filter cho JWT
                        .uri("lb://BOOK-SERVICE")) // Dịch vụ book-service sử dụng load balancer
                .route("borrowing-service", r -> r.path("/api/v1/borrowing-service/**")
                        .filters(f -> f.filter(filter)) // Áp dụng filter cho JWT
                        .uri("lb://BORROWING-SERVICE")) // Dịch vụ book-service sử dụng load balancer
                .route("user-service", r -> r.path("/api/v1/user-service/**")
                        .filters(f -> f.filter(filter)) // Áp dụng filter cho JWT
                        .uri("lb://USER-SERVICE")) // Dịch vụ user-service sử dụng load balancer
                .route("inventory-service", r -> r.path("/api/v1/inventory-service/**")
                        .filters(f -> f.filter(filter)) // Áp dụng filter cho JWT
                        .uri("lb://INVENTORY-SERVICE")) // Dịch vụ inventory-service sử dụng load balancer
                .route("notification-service", r -> r.path("/api/v1/notification-service/**")
                        .filters(f -> f.filter(filter)) // Áp dụng filter cho JWT
                        .uri("lb://NOTIFICATION-SERVICE")) // Dịch vụ notification-service sử dụng load balancer
                .route("recommendation-service", r -> r.path("/api/v1/recommendation-service/**")
                        .filters(f -> f.filter(filter)) // Áp dụng filter cho JWT
                        .uri("lb://RECOMMENDATION-SERVICE")) // Dịch vụ notification-service sử dụng load balancer
                .build(); // Kết thúc việc cấu hình các route
    }
}
