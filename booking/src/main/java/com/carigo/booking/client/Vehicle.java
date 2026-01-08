package com.carigo.booking.client;

import java.time.LocalDateTime;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.carigo.booking.config.FeignConfig;
import com.carigo.booking.dto.PriceQuoteDto;
import com.carigo.booking.dto.PriceQuoteRequest;
import com.carigo.booking.dto.UserAndVehicleVerify;

@FeignClient(name = "vehicle", url = "http://localhost:8083/vehicles", configuration = FeignConfig.class)
public interface Vehicle {

        @PostMapping("/userandvehicle")
        Boolean userAndVehicle(@RequestBody UserAndVehicleVerify dto);

        @GetMapping("/exitvehicle/{id}")
        Boolean exitVehicle(@PathVariable("id") String id);

        @PostMapping("/{id}/quote")
        PriceQuoteDto getUpdatedQuote(
                        @PathVariable("id") String vehicleId,
                        @RequestBody PriceQuoteRequest request);

        @GetMapping("/{id}/maintenance/check")
        Boolean checkMaintenance(
                        @PathVariable("id") String vehicleId,
                        @RequestParam("start") LocalDateTime start,
                        @RequestParam("end") LocalDateTime end);

        // OPTIONAL - if you add review system in Vehicle MS
        /*
         * @PostMapping("/{id}/reviews")
         * Boolean saveReview(
         * 
         * @PathVariable("id") String vehicleId,
         * 
         * @RequestBody ReviewRequest review
         * );
         */
}
