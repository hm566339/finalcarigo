package com.carigo.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.booking.config.FeignConfig;

@FeignClient(name = "profile", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface UserVerify {

    @GetMapping("/renters/user/{id}")
    Boolean exitRenters(@PathVariable("id") Long id);

}
