package com.carigo.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "vehicle-service", url = "http://localhost:8083")
public interface VehicleClient {

    @GetMapping("/vehicles/count")
    long countAll();

    @GetMapping("/vehicles/count/active")
    long countActive();

    @GetMapping("/vehicles/count/blocked")
    long countBlocked();

    @GetMapping("/vehicles/count/kyc/pending")
    long countPendingKyc();

    @GetMapping("/vehicles/insurance/expiring")
    long insuranceExpiringSoon();
}
