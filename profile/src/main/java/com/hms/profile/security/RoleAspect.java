package com.hms.profile.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAspect {

    private final HttpServletRequest request;

    @Before("@annotation(roleRequired)")
    public void checkRole(JoinPoint joinPoint, RoleRequired roleRequired) {

        String requiredRole = roleRequired.value();
        String userRole = request.getHeader("X-USER-ROLE");

        if (userRole == null) {
            throw new RuntimeException("ACCESS_DENIED: No role provided");
        }

        if (!userRole.equalsIgnoreCase(requiredRole) &&
                !userRole.equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("ACCESS_DENIED: You cannot access this endpoint");
        }

        System.out.println("Role check passed for: " + requiredRole);
    }
}
