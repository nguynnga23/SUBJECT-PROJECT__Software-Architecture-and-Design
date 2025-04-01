package com.gatewayservice.filter;

import com.gatewayservice.exception.JwtTokenMalformedException;
import com.gatewayservice.exception.JwtTokenMissingException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtAuthenticator jwtAuthenticator;

    @Autowired
    private AuthorizationChecker authorizationChecker;

    @Autowired
    private ResponseHandler responseHandler;

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/v1/user-service/users/login",
            "/api/v1/user-service/users/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().name();
        System.out.println("Request path: " + path + ", Method: " + method);

        Predicate<ServerHttpRequest> isApiSecured = r -> PUBLIC_ENDPOINTS.stream()
                .noneMatch(uri -> r.getURI().getPath().equals(uri));

        if (!isApiSecured.test(request)) {
            System.out.println("Public endpoint, skipping authentication: " + path);
            return chain.filter(exchange);
        }

        try {
            Claims claims = jwtAuthenticator.authenticate(request);
            String role = claims.get("role", String.class);
            String userId = String.valueOf(claims.get("id"));

            authorizationChecker.checkAuthorization(path, method, role);

            request = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(request).build());

        } catch (JwtTokenMalformedException | JwtTokenMissingException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return responseHandler.badRequestResponse(exchange);
        } catch (SecurityException e) {
            System.out.println("Authorization failed: " + e.getMessage());
            return responseHandler.forbiddenResponse(exchange, e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return responseHandler.unauthorizedResponse(exchange);
        }
    }
}