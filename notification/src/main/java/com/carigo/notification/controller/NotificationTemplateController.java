package com.carigo.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carigo.notification.dto.NotificationTemplateRequest;
import com.carigo.notification.model.NotificationTemplate;
import com.carigo.notification.service.NotificationTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    // 🔒 ADMIN ONLY
    @PostMapping("/template")
    public ResponseEntity<NotificationTemplate> create(
            @RequestBody NotificationTemplateRequest request) {

        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/template/{id}")
    public ResponseEntity<NotificationTemplate> update(
            @PathVariable Long id,
            @RequestBody NotificationTemplateRequest request) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<NotificationTemplate>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(service.list(page, size));
    }

    @DeleteMapping("/template/{id}")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.ok("Template deactivated");
    }
}
