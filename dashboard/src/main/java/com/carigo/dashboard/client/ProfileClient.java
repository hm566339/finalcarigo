package com.carigo.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "profile-service", url = "http://localhost:8081")
public interface ProfileClient {

    @GetMapping("/car-owners/count")
    long countOwners();

    @GetMapping("/car-owners/count/kyc/pending")
    long pendingOwnerKyc();

    @GetMapping("/renters/count")
    long countRenters();

    @GetMapping("/renters/count/kyc/pending")
    long pendingRenterKyc();
}
