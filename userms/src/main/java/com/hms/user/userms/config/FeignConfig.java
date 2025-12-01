package com.hms.user.userms.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignConfig {

    @Bean
    public RequestInterceptor secretKeyInterceptor() {
        return requestTemplate -> {

            // Current HTTP request (jo Gateway ne forward kiya)
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            if (requestAttributes instanceof ServletRequestAttributes attrs) {
                HttpServletRequest req = attrs.getRequest();

                // 1️⃣ Forward Authorization (JWT from gateway)
                String auth = req.getHeader("Authorization");
                if (auth != null && !auth.isBlank()) {
                    requestTemplate.header("Authorization", auth);
                }
            }

            // 2️⃣ Always forward the gateway internal secret
            requestTemplate.header("X-SECRET-KEY", "SECRET");
        };
    }
}
