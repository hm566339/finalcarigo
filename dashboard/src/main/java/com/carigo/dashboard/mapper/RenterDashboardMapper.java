package com.carigo.dashboard.mapper;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

import com.carigo.dashboard.dto.BookingStats;
import com.carigo.dashboard.dto.CurrentTrip;
import com.carigo.dashboard.dto.RenterDashboardDTO;
import com.carigo.dashboard.dto.Spending;

@UtilityClass
public class RenterDashboardMapper {

    public RenterDashboardDTO map(
            Long renterId,
            Map<String, Object> renterProfile,
            BookingStats bookingStats,
            CurrentTrip currentTrip,
            Spending spending,
            double rating,
            List<String> alerts) {

        return RenterDashboardDTO.builder()
                .renterId(renterId)
                .renterName((String) renterProfile.get("name"))
                .kycStatus(String.valueOf(renterProfile.get("kycStatus")))

                .bookingStats(bookingStats)
                .currentTrip(currentTrip)
                .spending(spending)
                .averageRating(rating)

                .alerts(alerts)
                .build();
    }
}
