package com.hms.profile.controller;

import com.hms.profile.dto.RenterRequestDTO;
import com.hms.profile.dto.RenterResponseDTO;
import com.hms.profile.dto.ResponseDTO;
import com.hms.profile.dto.UserDTO;
import com.hms.profile.model.Renter;
import com.hms.profile.service.RenterService;

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
@RequestMapping("/renters")
public class RenterController {

    private final RenterService service;

    // ---------------------- CREATE ----------------------
    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestBody UserDTO dto) {
        service.createRenter(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO("USER_REGISTERED_SUCCESSFULLY"));
    }

    // ---------------------- GET PROFILE ----------------------
    @RolesAllowed("REANT")
    @GetMapping("/{id}")
    public ResponseEntity<RenterResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRenterById(id));
    }

    @RolesAllowed("REANT")
    @GetMapping("/email/{email}")
    public ResponseEntity<RenterResponseDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getRenterByEmail(email));
    }

    // ---------------------- UPDATE ----------------------
    @RolesAllowed("REANT")
    @PutMapping("/{id}")
    public ResponseEntity<RenterResponseDTO> update(
            @PathVariable Long id,
            @RequestBody RenterRequestDTO dto) {
        return ResponseEntity.ok(service.updateRenter(id, dto));
    }

    // ---------------------- KYC UPLOAD ----------------------
    @RolesAllowed("REANT")
    @PostMapping("/{id}/upload/aadhaar-front")
    public ResponseEntity<RenterResponseDTO> uploadFront(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadAadhaarFront(id, file));
    }

    @RolesAllowed("REANT")
    @PostMapping("/{id}/upload/aadhaar-back")
    public ResponseEntity<RenterResponseDTO> uploadBack(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadAadhaarBack(id, file));
    }

    @RolesAllowed("REANT")
    @PostMapping("/{id}/upload/selfie")
    public ResponseEntity<RenterResponseDTO> uploadSelfie(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadSelfie(id, file));
    }

    @RolesAllowed("REANT")
    @PostMapping("/{id}/upload/driving-license")
    public ResponseEntity<RenterResponseDTO> uploadLicense(
            @PathVariable Long id,
            @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadDrivingLicense(id, file));
    }

    // ---------------------- KYC FLOW ----------------------
    @RolesAllowed("REANT")
    @PostMapping("/{id}/kyc/verify")
    public ResponseEntity<RenterResponseDTO> verify(@PathVariable Long id) {
        return ResponseEntity.ok(service.verifyKyc(id));
    }

    @RolesAllowed("REANT")
    @PostMapping("/{id}/kyc/reject")
    public ResponseEntity<RenterResponseDTO> reject(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(service.markKycRejected(id, reason));
    }

    // ---------------------- DELETE ----------------------
    @RolesAllowed("REANT")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteRenter(id);
        return ResponseEntity.ok("Renter deleted successfully");
    }

    // ---------------------- LIST & SEARCH ----------------------
    @RolesAllowed("REANT")
    @GetMapping
    public ResponseEntity<List<RenterResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listRenters(page, size));
    }

    @RolesAllowed("REANT")
    @GetMapping("/search")
    public ResponseEntity<List<RenterResponseDTO>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    // ---------------------- RATING ----------------------
    @RolesAllowed("REANT")
    @PostMapping("/{id}/rating")
    public ResponseEntity<RenterResponseDTO> updateRating(
            @PathVariable Long id,
            @RequestParam double rating) {
        return ResponseEntity.ok(service.updateRating(id, rating));
    }

    // ---------------------- TRIP ----------------------
    @RolesAllowed("REANT")
    @PostMapping("/{id}/trips/increment")
    public ResponseEntity<String> increment(@PathVariable Long id) {
        service.incrementTripCount(id);
        return ResponseEntity.ok("Trip count updated");
    }

    @RolesAllowed("REANT")
    @GetMapping("/dl/{dl}")
    public ResponseEntity<Boolean> dlIsPresent(@PathVariable("dl") String dl) {
        return ResponseEntity.ok(service.isPresentDl(dl));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Boolean> userIDisPresent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.exitUserID(id));
    }

}
