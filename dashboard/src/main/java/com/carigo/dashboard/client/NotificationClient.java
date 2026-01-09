package com.carigo.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "NOTIFICATION-SERVICE", url = "localhost:8086")
public interface NotificationClient {

    @GetMapping("/notifications/user/{userId}/unread-count")
    long unreadCount(@PathVariable("userId") Long userId);

}
