package com.carigo.dashboard.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.carigo.dashboard.client.BookingClient;
import com.carigo.dashboard.client.NotificationClient;
import com.carigo.dashboard.client.ProfileClient;
import com.carigo.dashboard.dto.RenterDashboardDTO;
import com.carigo.dashboard.entity.BookingResponseDTO;
import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.CurrentTrip;
import com.carigo.dashboard.entity.ProfileKycHistory;
import com.carigo.dashboard.entity.RenterResponseDTO;
import com.carigo.dashboard.entity.Spending;
import com.carigo.dashboard.mapper.RenterDashboardMapper;
import com.carigo.dashboard.service.RenterDashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RenterDashboardServiceImpl implements RenterDashboardService {

    private final BookingClient bookingClient;
    private final ProfileClient profileClient;
    private final NotificationClient notificationClient;
    private final RenterDashboardMapper mapper;

    @Override
    public RenterDashboardDTO getRenterDashboard(Long renterId) {

        // 🧑 Renter profile
        RenterResponseDTO renter = profileClient.getRenterById(renterId);

        // 📊 Booking numbers
        long totalBookings = bookingClient.countTotal(renterId);
        long completedBookings = bookingClient.completedBookings(renterId);
        List<BookingResponseDTO> activeBookings = bookingClient.activeBookings(renterId);

        BookingStats bookingStats = BookingStats.builder()
                .total(totalBookings)
                .completed(completedBookings)
                .ongoing(activeBookings.size())
                .cancelled(0)
                .build();

        // 🚗 Current trip
        BookingResponseDTO currentBooking = bookingClient.currentRenterTrip(renterId);

        CurrentTrip currentTrip = null;
        if (currentBooking != null) {
            currentTrip = CurrentTrip.builder()
                    .bookingId(currentBooking.getId())
                    .vehicleName(currentBooking.getVehicleId()) // later improve
                    .vehicleNumber(currentBooking.getVehicleId())
                    .startTime(currentBooking.getStartDate())
                    .endTime(currentBooking.getEndDate())
                    .status(currentBooking.getStatus().name())
                    .build();
        }

        // 💰 Spending (placeholder)
        Spending spending = Spending.builder()
                .thisMonth(0.0)
                .lifetime(0.0)
                .build();

        // ⭐ Rating
        double averageRating = renter.getRating() != null ? renter.getRating() : 0.0;

        // 🚨 Alerts
        List<String> alerts = new ArrayList<>();

        long unreadNotifications = notificationClient.unreadCount(renterId);

        if (unreadNotifications > 0) {
            alerts.add("You have " + unreadNotifications + " unread notifications");
        }

        List<ProfileKycHistory> kycHistory = profileClient.renterKycHistory(renterId);

        if (!kycHistory.isEmpty()
                && "REJECTED".equalsIgnoreCase(
                        kycHistory.get(0).getStatus().name())) {
            alerts.add("Your KYC is rejected. Please re-submit documents.");
        }

        return mapper.toDashboard(
                renterId,
                renter.getName(),
                renter.getKycStatus(),
                bookingStats,
                currentTrip,
                spending,
                averageRating,
                alerts,
                totalBookings);
    }
}
