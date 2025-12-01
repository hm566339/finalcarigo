package com.carigo.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.carigo.booking.config.FeignConfig;
import com.carigo.booking.dto.UserAndVehicleVerify;

@FeignClient(name = "vehicle", url = "http://localhost:8083/vehicles", configuration = FeignConfig.class)
public interface Vehicle {

    @PostMapping("/userandvehicle")
    Boolean userAndVehicle(@RequestBody UserAndVehicleVerify dto);

    @GetMapping("exitvehicle/{id}")
    Boolean exitVehicle(@PathVariable("id") String id);

}
