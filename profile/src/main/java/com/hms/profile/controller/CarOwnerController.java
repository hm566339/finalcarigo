package com.hms.profile.controller;

import com.hms.profile.dto.CarOwnerRequestDTO;
import com.hms.profile.dto.CarOwnerResponseDTO;
import com.hms.profile.dto.ResponseDTO;
import com.hms.profile.dto.UpdateUser;
import com.hms.profile.dto.UserDTO;
import com.hms.profile.model.ProfileKycHistory;
import com.hms.profile.security.RoleRequired;
import com.hms.profile.service.CarOwnerService;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/car-owners")
public class CarOwnerController {

    private final CarOwnerService service;

    // --------------------- CREATE OWNER ---------------------
    @PostMapping
    public ResponseEntity<ResponseDTO> createOwner(@RequestBody UserDTO dto) {
        service.createOwner(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("USER_REGISTERED_SUCCESSFULLY"));
    }

    // --------------------- GET PROFILE ----------------------
    // @RolesAllowed("OWNER")
    @GetMapping("/{id}")
    public ResponseEntity<CarOwnerResponseDTO> getOwner(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOwnerById(id));
    }

    @RoleRequired("OWNER")
    @GetMapping("/email/{email}")
    public ResponseEntity<CarOwnerResponseDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getOwnerByEmail(email));
    }

    @RolesAllowed("OWNER") // --------------------- UPDATE PROFILE
    @PutMapping("/{id}")
    public ResponseEntity<CarOwnerResponseDTO> updateOwner(
            @PathVariable Long id,
            @RequestBody CarOwnerRequestDTO dto) {
        return ResponseEntity.ok(service.updateOwner(id, dto));
    }

    // ----------------- KYC DOCUMENT UPLOADS -----------------
    @RolesAllowed("OWNER")
    @PostMapping("/{id}/upload/aadhaar-front")
    public ResponseEntity<CarOwnerResponseDTO> uploadAadhaarFront(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadAadhaarFront(id, file));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/upload/aadhaar-back")
    public ResponseEntity<CarOwnerResponseDTO> uploadAadhaarBack(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadAadhaarBack(id, file));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/upload/selfie")
    public ResponseEntity<CarOwnerResponseDTO> uploadSelfie(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadSelfie(id, file));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/upload/driving-license")
    public ResponseEntity<CarOwnerResponseDTO> uploadDrivingLicense(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadDrivingLicense(id, file));
    }

    // ---------------------- KYC FLOW ----------------------
    @RolesAllowed("OWNER")
    @PostMapping("/{id}/kyc/verify")
    public ResponseEntity<CarOwnerResponseDTO> verifyKyc(@PathVariable Long id) {
        return ResponseEntity.ok(service.verifyKyc(id));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/kyc/reject")
    public ResponseEntity<CarOwnerResponseDTO> rejectKyc(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(service.markKycRejected(id, reason));
    }

    // ---------------------- WALLET --------------------------
    @RolesAllowed("OWNER")
    @PostMapping("/{id}/wallet/add")
    public ResponseEntity<CarOwnerResponseDTO> addToWallet(
            @PathVariable Long id,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.addToWallet(id, amount));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/wallet/deduct")
    public ResponseEntity<CarOwnerResponseDTO> deductFromWallet(
            @PathVariable Long id,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.deductFromWallet(id, amount));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/wallet/withdraw")
    public ResponseEntity<CarOwnerResponseDTO> withdraw(
            @PathVariable Long id,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.withdrawToBank(id, amount));
    }

    @RolesAllowed("OWNER")
    @GetMapping("/{id}/wallet/balance")
    public ResponseEntity<Double> walletBalance(@PathVariable Long id) {
        return ResponseEntity.ok(service.getWalletBalance(id));
    }

    // ---------------------- BANK DETAILS ---------------------
    @RolesAllowed("OWNER")
    @PostMapping("/{id}/bank")
    public ResponseEntity<CarOwnerResponseDTO> updateBankDetails(
            @PathVariable Long id,
            @RequestParam String accountHolderName,
            @RequestParam String accountNumber,
            @RequestParam String ifscCode) {

        return ResponseEntity.ok(
                service.updateBankDetails(id, accountHolderName, accountNumber, ifscCode));
    }

    // ---------------------- RATING & TRIPS ---------------------
    @RolesAllowed("OWNER")
    @PostMapping("/{id}/rating")
    public ResponseEntity<CarOwnerResponseDTO> updateRating(
            @PathVariable Long id,
            @RequestParam double rating) {

        return ResponseEntity.ok(service.updateRating(id, rating));
    }

    @RolesAllowed("OWNER")
    @PostMapping("/{id}/trips/increment")
    public ResponseEntity<String> incrementTrips(@PathVariable Long id) {
        service.incrementCompletedTrips(id);
        return ResponseEntity.ok("Trip count updated");
    }

    // ----------------------- LIST & SEARCH ----------------------
    @RolesAllowed("OWNER")
    @GetMapping
    public ResponseEntity<List<CarOwnerResponseDTO>> listOwners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(service.listOwners(page, size));
    }

    @RolesAllowed("OWNER")
    @GetMapping("/search")
    public ResponseEntity<List<CarOwnerResponseDTO>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    // ------------------------ DELETE --------------------------
    @RolesAllowed("OWNER")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOwner(@PathVariable Long id) {
        service.deleteOwnerByUserId(id);
        return ResponseEntity.ok("Owner deleted successfully");
    }

    @RolesAllowed("OWNER")
    @GetMapping("/is-present/{id}")
    public ResponseEntity<Boolean> isPresent(@PathVariable Long id) {
        boolean exists = service.verifyUser(id);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/car-owners/update/email-name/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UpdateUser user) {
        return ResponseEntity.ok(service.UpdateEmail_name(id, user));
    }

    @GetMapping("/{id}/aadhaar-front-url")
    public ResponseEntity<String> getAadharFornt(@PathVariable Long id) {
        return ResponseEntity.ok(service.aadharFront(id));
    }

    @GetMapping("/{id}/aadhaar-back-url")
    public ResponseEntity<String> getAadharBack(@PathVariable Long id) {
        return ResponseEntity.ok(service.aadharBack(id));
    }

    @GetMapping("/{id}/selfie-url")
    public ResponseEntity<String> getSelfie(@PathVariable Long id) {
        return ResponseEntity.ok(service.selfie(id));
    }

    @GetMapping("/admin/car-owners")
    public ResponseEntity<List<CarOwnerResponseDTO>> getAllOwners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        return ResponseEntity.ok(service.getAllOwners(page, size));
    }

    @RolesAllowed({ "OWNER", "ADMIN" })
    @GetMapping("/{id}/kyc/history")
    public ResponseEntity<List<ProfileKycHistory>> getKycHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getOwnerKycHistory(id));
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<CarOwnerResponseDTO> blockOwner(@PathVariable Long id) {
        // vehicle ko bhi block krna hai
        return ResponseEntity.ok(service.blockOwner(id));
    }

    @PostMapping("/unblock/{id}")
    public ResponseEntity<CarOwnerResponseDTO> unblockOwner(@PathVariable Long id) {
        return ResponseEntity.ok(service.unblockOwner(id));
    }

    @GetMapping("/count")
    public long countOwners() {
        return service.countOwners();
    }

    @GetMapping("/count/kyc/pending")
    public long pendingOwnerKyc() {
        return service.pendingOwnerKyc();
    }

}
