package com.carigo.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.carigo.booking.config.FeignConfig;
import com.carigo.booking.dto.NotificationRequest;

@FeignClient(name = "notification", url = "http://localhost:8085", configuration = FeignConfig.class)
public interface NotificationClient {

        @PostMapping("/notifications/send")
        void sendNotification(@RequestBody NotificationRequest request);
}
