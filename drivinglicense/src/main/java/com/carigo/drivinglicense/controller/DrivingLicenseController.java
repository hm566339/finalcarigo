package com.carigo.drivinglicense.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carigo.drivinglicense.dto.AddDrivingLicenseRequest;
import com.carigo.drivinglicense.dto.DrivingLicenseDto;
import com.carigo.drivinglicense.helper.KycStatus;
import com.carigo.drivinglicense.model.DlKycHistory;
import com.carigo.drivinglicense.service.DlExternalClient;
import com.carigo.drivinglicense.service.DrivingLicenseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dl")
@CrossOrigin
@AllArgsConstructor
public class DrivingLicenseController {

    private final DrivingLicenseService service;
    private final DlExternalClient externalClient;

    @PostMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<DrivingLicenseDto> create(@PathVariable("id") Long id,
            @ModelAttribute @Valid AddDrivingLicenseRequest req) throws IOException {

        return ResponseEntity.ok(service.add(id, req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrivingLicenseDto> get(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-number/{dl}")
    public ResponseEntity<DrivingLicenseDto> getByDl(@PathVariable String dl) {
        return ResponseEntity.ok(service.getByDlNumber(dl));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<DrivingLicenseDto> update(
            @PathVariable String id,
            @ModelAttribute AddDrivingLicenseRequest req) throws IOException {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().body("deleted");
    }

    @PostMapping("/{id}/verify")
    public ResponseEntity<DrivingLicenseDto> verify(@PathVariable String id) {

        DrivingLicenseDto local = service.getById(id);

        var external = externalClient.fetchByDlNumber(local.getDlNumber());

        return ResponseEntity.ok(service.verify(id, external));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<DrivingLicenseDto> getMethodName(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getByUserId(id));
    }

    @GetMapping("/admin/dl")
    public ResponseEntity<List<DrivingLicenseDto>> listAllDl(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/admin/dl/status/{status}")
    public ResponseEntity<List<DrivingLicenseDto>> getByStatus(
            @PathVariable KycStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.getByStatus(status, page, size));
    }

    @GetMapping("/dl/{dlId}/kyc/history")
    public ResponseEntity<List<DlKycHistory>> getDlKycHistory(
            @PathVariable String dlId) {

        return ResponseEntity.ok(service.getKycHistory(dlId));
    }

    @PostMapping("/admin/dl/{dlId}/kyc/override")
    public ResponseEntity<DrivingLicenseDto> adminOverride(
            @PathVariable String dlId,
            @RequestParam KycStatus status,
            @RequestParam String reason) {

        return ResponseEntity.ok(
                service.adminKycOverride(dlId, status, reason));
    }

    @GetMapping("/admin/dl/expiring")
    public ResponseEntity<List<DrivingLicenseDto>> expiringDl(
            @RequestParam(defaultValue = "30") int days) {

        return ResponseEntity.ok(service.getExpiringDl(days));
    }

    @PostMapping("/dl/{id}/reverify")
    public ResponseEntity<DrivingLicenseDto> reverify(@PathVariable String id) {

        return ResponseEntity.ok(service.reverify(id));
    }

    @DeleteMapping("/dl/{id}/images")
    public ResponseEntity<String> deleteImages(@PathVariable String id) {

        service.deleteImages(id);
        return ResponseEntity.ok("DL images deleted");
    }

}
