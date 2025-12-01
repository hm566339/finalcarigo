package com.carigo.vehicle.controller;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.UserAndVehicleVerify;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.service.VehicleService;
import com.carigo.vehicle.service.VerificationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin
@Validated
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VerificationService verificationService;

    // CREATE VEHICLE
    @PostMapping(value = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createVehicleMultipart(
            @PathVariable Long userId,
            @ModelAttribute AddVehicleRequest request) throws IOException {

        vehicleService.createVehicleMultipart(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Vehicle created successfully with images");
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable String id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // GET BY USER ID
    @GetMapping("/by-number/{number}")
    public ResponseEntity<List<VehicleDto>> getByNumber(@PathVariable Long number) {
        return ResponseEntity.ok(vehicleService.getByUserId(number));
    }

    // LIST VEHICLES
    @GetMapping("/all")
    public ResponseEntity<List<VehicleDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(vehicleService.listAllVehicles(page, size));
    }

    // UPDATE VEHICLE
    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<VehicleDto> updateVehicle(
            @PathVariable String id,
            @ModelAttribute AddVehicleRequest request) throws IOException {

        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }

    // DELETE VEHICLE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }

    // VERIFY KYC
    @PostMapping("/{id}/verify")
    public ResponseEntity<VehicleDto> verify(@PathVariable String id) {

        VehicleDto local = vehicleService.getVehicleById(id);

        Map<String, Object> external = verificationService.fetchExternalData(local.getVehicleNumber());

        VehicleDto result = vehicleService.verifyVehicle(id, external);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/exitvehicle/{id}")
    public ResponseEntity<Boolean> exitVehile(@PathVariable("id") String id) {
        return ResponseEntity.ok(vehicleService.exitVehicleNumber(id));
    }

    @PostMapping("/userandvehicle")
    public ResponseEntity<Boolean> userAndVehicle(@RequestBody UserAndVehicleVerify dto) {
        return ResponseEntity.ok(vehicleService.verifyUserAndVehicle(dto));
    }

}
