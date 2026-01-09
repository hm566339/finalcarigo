package com.carigo.dashboard.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.dashboard.entity.CarOwnerResponseDTO;
import com.carigo.dashboard.entity.ProfileKycHistory;
import com.carigo.dashboard.entity.RenterResponseDTO;

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

    @GetMapping("/car-owners/{id}")
    CarOwnerResponseDTO getOwnerById(@PathVariable("id") Long ownerId);

    @GetMapping("/car-owners/{id}/wallet/balance")
    Double getWalletBalance(@PathVariable("id") Long ownerId);

    @GetMapping("/car-owners/{id}/wallet/balance")
    Double walletBalance(@PathVariable("id") Long ownerId);

    // 🧑 renter profile
    @GetMapping("/renters/{id}")
    RenterResponseDTO getRenterById(@PathVariable Long id);

    @GetMapping("/renters/{id}/kyc/history")
    List<ProfileKycHistory> renterKycHistory(@PathVariable Long id);
}
