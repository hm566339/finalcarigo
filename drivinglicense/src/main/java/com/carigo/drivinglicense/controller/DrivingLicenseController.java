package com.carigo.drivinglicense.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carigo.drivinglicense.dto.AddDrivingLicenseRequest;
import com.carigo.drivinglicense.dto.DrivingLicenseDto;
import com.carigo.drivinglicense.service.DlExternalClient;
import com.carigo.drivinglicense.service.DrivingLicenseService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
