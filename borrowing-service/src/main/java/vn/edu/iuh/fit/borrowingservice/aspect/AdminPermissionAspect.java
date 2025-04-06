package vn.edu.iuh.fit.borrowingservice.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class AdminPermissionAspect {

    @Before("@annotation(vn.edu.iuh.fit.borrowingservice.annotation.RequireAdmin)")
    public void checkAdminPermission(JoinPoint joinPoint) {
        // Lấy HttpServletRequest từ context
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Lấy librarianId từ header
        String librarianId = request.getHeader("X-User-Id");
        if (librarianId == null || librarianId.isEmpty()) {
            throw new IllegalArgumentException("Missing Reader ID in request header.");
        }

        // Lấy roles từ header
        String roles = request.getHeader("X-User-Role");
        if (roles == null || !roles.contains("ADMIN")) {
            throw new IllegalArgumentException("Only admins can perform this action.");
        }
    }
}