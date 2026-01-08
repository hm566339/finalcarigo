package com.carigo.dashboard.component;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardKafkaListener {

    private final CacheManager cacheManager;

    @KafkaListener(topics = {
            "BOOKING_CREATED",
            "BOOKING_UPDATED",
            "PAYMENT_SUCCESS",
            "TRIP_STARTED",
            "TRIP_ENDED"
    })
    public void invalidateDashboardCache(String message) {

        // 🔥 Clear all dashboard caches
        if (cacheManager.getCache("adminDashboard") != null) {
            cacheManager.getCache("adminDashboard").clear();
        }
        if (cacheManager.getCache("ownerDashboard") != null) {
            cacheManager.getCache("ownerDashboard").clear();
        }
        if (cacheManager.getCache("renterDashboard") != null) {
            cacheManager.getCache("renterDashboard").clear();
        }
    }
}
