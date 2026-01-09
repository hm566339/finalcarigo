package com.carigo.dashboard.service.impl;

import com.carigo.dashboard.client.BookingClient;
import com.carigo.dashboard.client.ProfileClient;
import com.carigo.dashboard.client.VehicleClient;
import com.carigo.dashboard.config.JwtAuthFilter;
import com.carigo.dashboard.dto.OwnerDashboardResponse;
import com.carigo.dashboard.entity.*;
import com.carigo.dashboard.mapper.OwnerDashboardMapper;
import com.carigo.dashboard.service.OwnerDashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerDashboardServiceImpl implements OwnerDashboardService {

        private static final String CACHE_KEY_PREFIX = "OWNER:DASHBOARD:";

        // ✅ Feign clients
        private final ProfileClient profileClient;
        private final BookingClient bookingClient;
        private final VehicleClient vehicleClient;

        // ✅ Redis
        private final RedisTemplate<String, OwnerDashboardResponse> redisTemplate;

        // ✅ JWT helper
        private final JwtAuthFilter jwtUtil;

        @Override
        public OwnerDashboardResponse getOwnerDashboard(String authHeader) {

                // 🔐 Extract ownerId from JWT
                Long ownerId = jwtUtil.extractUserId(authHeader);

                if (ownerId == null) {
                        throw new RuntimeException("Invalid JWT : ownerId missing");
                }

                String cacheKey = CACHE_KEY_PREFIX + ownerId;

                // 1️⃣ Redis cache check
                OwnerDashboardResponse cached = redisTemplate.opsForValue().get(cacheKey);
                if (cached != null) {
                        log.info("✅ Owner dashboard cache HIT for ownerId={}", ownerId);
                        return cached;
                }

                log.info("❌ Owner dashboard cache MISS for ownerId={}", ownerId);

                // 2️⃣ Profile service
                var ownerProfile = profileClient.getOwnerById(ownerId);

                // 3️⃣ Booking service
                BookingStats bookingStats = bookingClient.bookingStats(ownerId);
                CurrentTrip currentTrip = bookingClient.currentTrip(ownerId);

                // 4️⃣ Vehicle service
                VehicleStats vehicleStats = vehicleClient.vehicleStats(ownerId);

                // 5️⃣ Earnings
                Double walletBalance = profileClient.walletBalance(ownerId);
                Earnings earnings = Earnings.builder()
                                .walletBalance(walletBalance != null ? walletBalance : 0.0)
                                .build();

                // 6️⃣ Alerts
                OwnerAlerts alerts = OwnerAlerts.builder()
                                .insuranceExpiring(vehicleClient.insuranceExpiring(ownerId))
                                .vehicleKycPending(vehicleClient.vehicleKycPending())
                                .payoutPending(0)
                                .disputes(bookingClient.disputeCount(ownerId))
                                .build();

                // 7️⃣ Owner summary
                OwnerSummary ownerSummary = OwnerDashboardMapper.buildOwnerSummary(
                                ownerId,
                                ownerProfile.getName(),
                                ownerProfile.getRating(),
                                ownerProfile.getKycStatus().name(),
                                ownerProfile.isBlocked());

                // 8️⃣ Final response
                OwnerDashboardResponse response = OwnerDashboardMapper.mapToDashboard(
                                ownerSummary,
                                bookingStats,
                                vehicleStats,
                                earnings,
                                currentTrip,
                                alerts);

                // 9️⃣ Cache result (5 minutes)
                redisTemplate.opsForValue()
                                .set(cacheKey, response, Duration.ofMinutes(5));

                return response;
        }
}
