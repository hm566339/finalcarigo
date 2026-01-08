package com.carigo.vehicle.controller;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.AvailabilityRequest;
import com.carigo.vehicle.dto.AvailabilitySlotDto;
import com.carigo.vehicle.dto.PriceQuoteDto;
import com.carigo.vehicle.dto.PriceQuoteRequest;
import com.carigo.vehicle.dto.UserAndVehicleVerify;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.helper.VehicleStatus;
import com.carigo.vehicle.model.KycHistory;
import com.carigo.vehicle.model.VehicleMaintenance;
import com.carigo.vehicle.service.AvailabilityService;
import com.carigo.vehicle.service.PricingService;
import com.carigo.vehicle.service.VehicleService;
import com.carigo.vehicle.service.VerificationService;
import com.carigo.vehicle.service.imp.KycHistoryService;
import com.carigo.vehicle.service.imp.MaintenanceService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@Validated
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VerificationService verificationService;
    private final AvailabilityService availabilityService;
    private final PricingService pricingService;
    private final MaintenanceService maintenanceService;
    private final KycHistoryService kycHistoryService;

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

    // GET availability
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<AvailabilitySlotDto>> getAvailability(
            @PathVariable String id,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        return ResponseEntity.ok(availabilityService.getAvailability(id, from, to));
    }

    // POST new slot
    @PostMapping("/{id}/availability")
    public ResponseEntity<AvailabilitySlotDto> addAvailabilitySlot(
            @PathVariable String id,
            @RequestBody AvailabilityRequest request) {

        request.setVehicleId(id);
        AvailabilitySlotDto slot = availabilityService.addSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(slot);
    }

    @PostMapping("/{id}/quote")
    public ResponseEntity<PriceQuoteDto> getPriceQuote(
            @PathVariable String id,
            @RequestBody PriceQuoteRequest request) {

        PriceQuoteDto quote = pricingService.calculateQuote(id, request);
        return ResponseEntity.ok(quote);
    }

    @PostMapping("/{id}/maintenance")
    public ResponseEntity<?> addMaintenance(
            @PathVariable String id,
            @RequestBody VehicleMaintenance request) {

        request.setVehicleId(id);
        return ResponseEntity.ok(maintenanceService.addMaintenance(request));
    }

    @GetMapping("/{id}/maintenance/check")
    public ResponseEntity<Boolean> checkMaintenance(
            @PathVariable String id,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ResponseEntity.ok(maintenanceService.isInMaintenance(id, start, end));
    }

    @GetMapping("/{id}/kyc-history")
    public ResponseEntity<List<KycHistory>> getKycHistory(@PathVariable String id) {
        return ResponseEntity.ok(kycHistoryService.getHistory(id));
    }

    @DeleteMapping("/{id}/availability")
    public ResponseEntity<String> clearAvailability(@PathVariable String id) {
        availabilityService.clearAvailability(id);
        return ResponseEntity.ok("Availability cleared");
    }

    @PostMapping("/{id}/kyc/admin-override")
    public ResponseEntity<VehicleDto> adminOverride(
            @PathVariable String id,
            @RequestParam KycStatus status,
            @RequestParam String reason) {

        return ResponseEntity.ok(
                vehicleService.adminKycOverride(id, status, reason));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<VehicleDto> changeStatus(
            @PathVariable String id,
            @RequestParam VehicleStatus status) {

        return ResponseEntity.ok(vehicleService.changeStatus(id, status));
    }

    @GetMapping("/count")
    public long countAll() {
        return vehicleService.countAllVehicles();
    }

    @GetMapping("/count/active")
    public long countActive() {
        return vehicleService.countActiveVehicles();
    }

    @GetMapping("/count/blocked")
    public long countBlocked() {
        return vehicleService.countBlockedVehicles();
    }

    @GetMapping("/count/kyc/pending")
    public long countPendingKyc() {
        return vehicleService.countPendingKycVehicles();
    }

    @GetMapping("/insurance/expiring")
    public long insuranceExpiringSoon() {
        return vehicleService.countInsuranceExpiringSoon();
    }

}
