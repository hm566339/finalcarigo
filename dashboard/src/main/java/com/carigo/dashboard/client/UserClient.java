package com.carigo.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.carigo.dashboard.component.UserClientFallback;

@FeignClient(name = "user-service", url = "http://localhost:8080", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/user/count")
    long countAll();

    @GetMapping("/user/count/blocked")
    long countBlocked();
}
