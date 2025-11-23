package com.hms.profile.controller;

import com.hms.profile.service.AadhaarKycService;
import com.hms.profile.repository.KycJobRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
public class ProfileKycController {

    private final AadhaarKycService aadhaarKycService;
    private final KycJobRepository jobRepo;

    // Enqueue owner KYC (returns job id)
    @PostMapping("/owner/{id}/aadhaar/verify")
    public ResponseEntity<?> verifyOwnerKyc(@PathVariable Long id) {
        Long jobId = aadhaarKycService.enqueueVerificationForOwner(id);
        return ResponseEntity.accepted().body(Map.of("jobId", jobId));
    }

    // Enqueue renter KYC
    @PostMapping("/renter/{id}/aadhaar/verify")
    public ResponseEntity<?> verifyRenterKyc(@PathVariable Long id) {
        Long jobId = aadhaarKycService.enqueueVerificationForRenter(id);
        return ResponseEntity.accepted().body(Map.of("jobId", jobId));
    }

    // Job status
    @GetMapping("/job/{id}")
    public ResponseEntity<?> jobStatus(@PathVariable Long id) {
        return jobRepo.findById(id)
                .map(j -> ResponseEntity.ok(j))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
