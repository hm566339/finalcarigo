package com.carigo.dashboard.mapper;

import org.springframework.stereotype.Component;

import com.carigo.dashboard.dto.RenterDashboardDTO;
import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.CurrentTrip;
import com.carigo.dashboard.entity.Spending;

import java.util.List;

@Component
public class RenterDashboardMapper {

    public RenterDashboardDTO toDashboard(
            Long renterId,
            String renterName,
            String kycStatus,
            BookingStats bookingStats,
            CurrentTrip currentTrip,
            Spending spending,
            double averageRating,
            List<String> alerts,
            long totalBookings) {

        return RenterDashboardDTO.builder()
                .renterId(renterId)
                .renterName(renterName)
                .kycStatus(kycStatus)
                .bookingStats(bookingStats)
                .currentTrip(currentTrip)
                .spending(spending)
                .averageRating(averageRating)
                .alerts(alerts)
                .totalBookings(totalBookings)
                .build();
    }
}
