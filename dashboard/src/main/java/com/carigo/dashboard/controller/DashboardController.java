package com.carigo.dashboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carigo.dashboard.config.JwtAuthFilter;
import com.carigo.dashboard.dto.AdminDashboardDTO;
import com.carigo.dashboard.dto.OwnerDashboardResponse;
import com.carigo.dashboard.dto.RenterDashboardDTO;
import com.carigo.dashboard.service.AdminDashboardService;
import com.carigo.dashboard.service.OwnerDashboardService;
import com.carigo.dashboard.service.RenterDashboardService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final AdminDashboardService adminDashboardService;

    private final OwnerDashboardService ownerDashboardService;

    private final RenterDashboardService renterDashboardService;

    private final JwtAuthFilter jwtAuthFilter;

    @GetMapping("/admin")
    public ResponseEntity<AdminDashboardDTO> adminDashboardDTO() {
        return ResponseEntity.ok(adminDashboardService.getAdminDashboard());
    }

    @GetMapping("/owner")
    public ResponseEntity<OwnerDashboardResponse> getOwnerDashboard(
            @RequestHeader("Authorization") String authorization) {

        OwnerDashboardResponse response = ownerDashboardService.getOwnerDashboard(authorization);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/renter")
    public RenterDashboardDTO dashboard(
            @RequestHeader("Authorization") String authHeader) {

        // ✅ JWT se renterId nikalo
        Long renterId = jwtAuthFilter.extractUserId(authHeader);

        return renterDashboardService.getRenterDashboard(renterId);
    }

}
