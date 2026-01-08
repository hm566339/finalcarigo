package com.carigo.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.booking.config.FeignConfig;
import com.carigo.booking.dto.CarOwnerResponseDTO;
import com.carigo.booking.dto.RenterResponseDTO;

@FeignClient(name = "profile", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface UserVerify {

    // ---------------- RENTER EXIST CHECK ----------------
    @GetMapping("/renters/user/{id}")
    Boolean exitRenters(@PathVariable("id") Long id);

    @GetMapping("/is-present/{id}")
    Boolean isPresent(@PathVariable Long id);

    // ---------------- GET OWNER DETAILS ----------------
    @GetMapping("/car-owners/{id}")
    CarOwnerResponseDTO getOwner(@PathVariable("id") Long id);

    // ---------------- GET RENTER DETAILS ----------------
    @GetMapping("/renters/{id}")
    RenterResponseDTO getRenter(@PathVariable("id") Long id);
}
