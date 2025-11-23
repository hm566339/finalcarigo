package com.carigo.vehicle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.vehicle.config.FeignConfig;

@FeignClient(name = "userms", url = "http://localhost:8080", configuration = FeignConfig.class)
public interface UserVerify {

    @GetMapping("/user/{id}")
    Boolean verifyUserExists(@PathVariable("id") Long id);
}
