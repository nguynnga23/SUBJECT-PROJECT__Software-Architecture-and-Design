package com.gatewayservice.filter;

import org.springframework.stereotype.Component;

@Component
public class AuthorizationChecker {
    public void checkAuthorization(String path, String method, String role) {
        if (path.startsWith("/api/v1/book-service") && !"ADMIN".equals(role) && isModificationMethod(method)) {
            throw new SecurityException("Chỉ Admin mới có quyền quản lý sách");
        }
        if (path.startsWith("/api/v1/inventory-service") && !"ADMIN".equals(role)) {
            throw new SecurityException("Chỉ Admin mới có quyền quản lý kho");
        }
    }

    private boolean isModificationMethod(String method) {
        return "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method);
    }
}
