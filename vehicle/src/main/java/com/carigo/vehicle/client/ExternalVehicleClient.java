package com.carigo.vehicle.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carigo.vehicle.config.VehicleFeignConfig;

import java.util.Map;

@FeignClient(name = "vehicle-verification", url = "${external.vehicle.api.url}", configuration = VehicleFeignConfig.class)
public interface ExternalVehicleClient {

    @GetMapping
    Map<String, Object> fetchByNumber(@RequestParam("number") String vehicleNumber);
}
