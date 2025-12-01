package com.hms.profile.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hms.profile.config.FeignConfig;

@FeignClient(name = "drivinglicense", url = "http://localhost:8083", configuration = FeignConfig.class)
public interface VehicleUserId {

    @PostMapping("/vehicles/create/{userId}")
    void createVehicle(@PathVariable("userId") Long userId);

}
