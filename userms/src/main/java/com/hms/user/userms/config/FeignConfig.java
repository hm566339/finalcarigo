package com.hms.user.userms.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignConfig {

    @Bean
    public RequestInterceptor secretKeyInterceptor() {
        return requestTemplate -> {

            // Current HTTP request ko fetch karna
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();

                // 1️ Forward Authorization (JWT)
                String auth = req.getHeader("Authorization");
                if (auth != null) {
                    requestTemplate.header("Authorization", auth);
                }
            }

            // 2️ Always forward X-SECRET-KEY
            requestTemplate.header("X-SECRET-KEY", "SECRET");
        };
    }
}
