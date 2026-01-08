package com.carigo.vehicle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.vehicle.config.FeignConfig;

@FeignClient(name = "profile", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface UserVerify {

    @GetMapping("/car-owners/is-present/{id}")
    Boolean verifyUserExists(@PathVariable("id") Long id);

    @GetMapping("/car-owners/is-presentvehicle/{vehicleId}")
    Boolean isPresentVehicle(@PathVariable("vehicleId") String vehicleId);
}
