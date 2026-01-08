package com.carigo.dashboard.service.impl;

import com.carigo.dashboard.client.BookingClient;
import com.carigo.dashboard.client.ProfileClient;
import com.carigo.dashboard.client.UserClient;
import com.carigo.dashboard.client.VehicleClient;
import com.carigo.dashboard.dto.AdminDashboardDTO;
import com.carigo.dashboard.dto.BookingStats;
import com.carigo.dashboard.dto.BookingTrendDTO;
import com.carigo.dashboard.dto.BookingTrends;
import com.carigo.dashboard.dto.KycStats;
import com.carigo.dashboard.dto.PlatformStats;
import com.carigo.dashboard.dto.VehicleStats;
import com.carigo.dashboard.mapper.AdminDashboardMapper;
import com.carigo.dashboard.service.AdminDashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

        private final UserClient userClient;
        private final ProfileClient profileClient;
        private final VehicleClient vehicleClient;
        private final BookingClient bookingClient;

        @Override
        @Cacheable(value = "adminDashboard", key = "'ADMIN'")
        public AdminDashboardDTO getAdminDashboard() {

                // ================= PLATFORM STATS =================
                PlatformStats platformStats = PlatformStats.builder()
                                .totalUsers(userClient.countAll())
                                .owners(profileClient.countOwners())
                                .renters(profileClient.countRenters())
                                .blockedUsers(userClient.countBlocked())
                                .build();

                // ================= VEHICLE STATS =================
                VehicleStats vehicleStats = VehicleStats.builder()
                                .total(vehicleClient.countAll())
                                .active(vehicleClient.countActive())
                                .pendingKyc(vehicleClient.countPendingKyc())
                                .blocked(vehicleClient.countBlocked())
                                .build();

                // ================= BOOKING STATS =================
                BookingStats bookingStats = BookingStats.builder()
                                .total(bookingClient.countAll())
                                .today(bookingClient.countToday())
                                .ongoing(bookingClient.countOngoing())
                                .upcoming(bookingClient.countUpcoming())
                                .completed(bookingClient.countCompleted())
                                .cancelled(bookingClient.countCancelled())
                                .disputed(bookingClient.countDisputed())
                                .build();

                // ================= BOOKING TRENDS (GRAPH) =================
                BookingTrends bookingTrends = buildBookingTrends();

                // ================= KYC STATS =================
                KycStats kycStats = KycStats.builder()
                                .ownerPending(profileClient.pendingOwnerKyc())
                                .renterPending(profileClient.pendingRenterKyc())
                                .vehiclePending(vehicleClient.countPendingKyc())
                                .build();

                // ================= ALERTS =================
                List<String> alerts = buildAlerts(
                                bookingClient.countPaymentPending(),
                                vehicleClient.insuranceExpiringSoon(),
                                bookingClient.staleDisputes());

                // ================= FINAL DASHBOARD =================
                return AdminDashboardMapper.map(
                                platformStats,
                                vehicleStats,
                                bookingStats,
                                bookingTrends,
                                kycStats,
                                alerts);
        }

        // ================= BOOKING TRENDS BUILDER =================
        private BookingTrends buildBookingTrends() {

                List<BookingTrendDTO> trends = bookingClient.bookingTrends();

                return BookingTrends.builder()
                                .labels(trends.stream().map(BookingTrendDTO::getDate).toList())
                                .total(trends.stream().map(BookingTrendDTO::getTotal).toList())
                                .completed(trends.stream().map(BookingTrendDTO::getCompleted).toList())
                                .cancelled(trends.stream().map(BookingTrendDTO::getCancelled).toList())
                                .build();
        }

        // ================= ALERT BUILDER =================
        private List<String> buildAlerts(
                        long paymentPending,
                        long insuranceExpiring,
                        long disputes) {

                return List.of(
                                paymentPending + " bookings stuck in payment",
                                insuranceExpiring + " vehicles insurance expiring",
                                disputes + " disputes pending > 48 hrs");
        }
}
