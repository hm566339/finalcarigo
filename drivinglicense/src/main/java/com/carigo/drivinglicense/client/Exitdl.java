package com.carigo.drivinglicense.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.drivinglicense.config.FeignConfig;

@FeignClient(name = "profile", url = "http://localhost:8081/renters", configuration = FeignConfig.class)
public interface Exitdl {

    @GetMapping("/dl/{dl}")
    Boolean dlIsPresent(@PathVariable("dl") String dl);

    @GetMapping("/user/{id}")
    Boolean verifyUserID(@PathVariable("id") Long id);

}
