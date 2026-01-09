package com.carigo.notification.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carigo.notification.dto.NotificationRequest;
import com.carigo.notification.model.NotificationLog;
import com.carigo.notification.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody @Valid NotificationRequest request) {
        notificationService.send(request);
        return ResponseEntity.ok("Notification processed");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationLog>> userNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getByUser(userId));
    }

    @GetMapping("/admin/notifications")
    public ResponseEntity<List<NotificationLog>> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(
                notificationService.getAllNotifications(page, size));
    }

    @GetMapping("/admin/notifications/failed")
    public ResponseEntity<List<NotificationLog>> failed() {

        return ResponseEntity.ok(
                notificationService.getFailedNotifications());
    }

    @PostMapping("/admin/notifications/retry/{id}")
    public ResponseEntity<String> retry(@PathVariable Long id) {

        notificationService.retry(id);
        return ResponseEntity.ok("Retry triggered");
    }

    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOtp(
            @RequestParam String phone,
            @RequestParam String otp) {

        notificationService.sendOtp(phone, otp);
        return ResponseEntity.ok("OTP sent");
    }

    @GetMapping("/admin/notifications/stats")
    public ResponseEntity<Map<String, Long>> stats() {
        return ResponseEntity.ok(notificationService.getNotificationStats());
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> unreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(
                notificationService.unreadCount(userId));
    }

}
