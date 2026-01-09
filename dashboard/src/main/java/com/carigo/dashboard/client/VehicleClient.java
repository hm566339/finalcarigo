package com.carigo.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.carigo.dashboard.entity.VehicleDto;
import com.carigo.dashboard.entity.VehicleStats;

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

    // 🚘 Vehicle statistics
    @GetMapping("/vehicles/owner/stats")
    VehicleStats vehicleStats(@RequestParam("ownerId") Long ownerId);

    // ⚠️ Insurance expiring soon
    @GetMapping("/vehicles/insurance/expiring/owner")
    long insuranceExpiring(@RequestParam("ownerId") Long ownerId);

    // ⚠️ Vehicle KYC pending
    @GetMapping("/vehicles/count/kyc/pending")
    long vehicleKycPending();

    @GetMapping("/vehicles/{id}")
    VehicleDto getVehicle(@PathVariable String id);
}
