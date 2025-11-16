package com.carigo.vehicle.controller;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.service.VehicleService;
import com.carigo.vehicle.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin
@Validated
public class VehicleController {

    private final VehicleService vehicleService;
    private final VerificationService verificationService;

    public VehicleController(VehicleService vehicleService, VerificationService verificationService) {
        this.vehicleService = vehicleService;
        this.verificationService = verificationService;
    }

    // CREATE VEHICLE
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<VehicleDto> addVehicle(
            @ModelAttribute @Valid AddVehicleRequest request) throws IOException {

        return ResponseEntity.ok(vehicleService.addVehicle(request));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable String id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // GET BY VEHICLE NUMBER
    @GetMapping("/by-number/{number}")
    public ResponseEntity<VehicleDto> getByNumber(@PathVariable String number) {
        return ResponseEntity.ok(vehicleService.getByVehicleNumber(number));
    }

    // LIST VEHICLES
    @GetMapping
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
}
