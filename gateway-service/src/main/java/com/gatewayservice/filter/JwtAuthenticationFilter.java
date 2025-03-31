package com.gatewayservice.filter;

import java.util.List;
import java.util.function.Predicate;

import com.gatewayservice.exception.JwtTokenMalformedException;
import com.gatewayservice.exception.JwtTokenMissingException;
import com.gatewayservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;



import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Cấu hình những endpoint không cần xác thực JWT (ví dụ: đăng nhập, đăng ký)
        final List<String> apiEndpoints = List.of("/register", "/login");

        // Kiểm tra xem request có phải là endpoint yêu cầu bảo mật hay không
        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        // Nếu là API cần bảo mật, thực hiện kiểm tra token
        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                // Nếu không có header Authorization thì trả về Unauthorized
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // Lấy token từ header Authorization
            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            try {
                // Kiểm tra tính hợp lệ của token
                jwtUtil.validateToken(token);
            } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
                // Nếu token không hợp lệ, trả về Bad Request
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }

            // Nếu token hợp lệ, lấy claims và thêm id vào header
            Claims claims = jwtUtil.getClaims(token);
            request = exchange.getRequest().mutate()
                    .header("id", String.valueOf(claims.get("id")))
                    .build();
        }

        // Tiếp tục xử lý request
        return chain.filter(exchange.mutate().request(request).build());
    }
}
