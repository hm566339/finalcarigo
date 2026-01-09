// package com.carigo.dashboard.controller;

// import com.carigo.dashboard.dto.OwnerDashboardResponse;
// import com.carigo.dashboard.service.OwnerDashboardService;

// import lombok.RequiredArgsConstructor;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/dashboard/owner")
// @RequiredArgsConstructor
// public class OwnerDashboardController {

// private final OwnerDashboardService ownerDashboardService;

// /**
// * 📱 OWNER DASHBOARD (MOBILE)
// * Header:
// * Authorization: Bearer <JWT>
// */
// @GetMapping
// public ResponseEntity<OwnerDashboardResponse> getOwnerDashboard(
// @RequestHeader("Authorization") String authorization) {

// OwnerDashboardResponse response =
// ownerDashboardService.getOwnerDashboard(authorization);

// return ResponseEntity.ok(response);
// }
// }
