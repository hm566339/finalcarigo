// package com.carigo.dashboard.controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.carigo.dashboard.dto.AdminDashboardDTO;
// import com.carigo.dashboard.service.AdminDashboardService;

// import lombok.AllArgsConstructor;

// @RestController
// @RequestMapping("/dashboard/admin")
// @AllArgsConstructor
// public class AdminController {

// private final AdminDashboardService adminDashboardService;

// @GetMapping
// public ResponseEntity<AdminDashboardDTO> adminDashboardDTO() {
// return ResponseEntity.ok(adminDashboardService.getAdminDashboard());
// }

// }
