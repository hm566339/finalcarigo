package com.carigo.dashboard.mapper;

import com.carigo.dashboard.dto.*;
import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.CurrentTrip;
import com.carigo.dashboard.entity.Earnings;
import com.carigo.dashboard.entity.OwnerAlerts;
import com.carigo.dashboard.entity.OwnerSummary;
import com.carigo.dashboard.entity.VehicleStats;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OwnerDashboardMapper {

    /**
     * Build full owner dashboard response
     */
    public static OwnerDashboardResponse mapToDashboard(
            OwnerSummary owner,
            BookingStats bookingStats,
            VehicleStats vehicleStats,
            Earnings earnings,
            CurrentTrip currentTrip,
            OwnerAlerts alerts) {

        return OwnerDashboardResponse.builder()
                .owner(owner)
                .bookingStats(bookingStats)
                .vehicleStats(vehicleStats)
                .earnings(earnings)
                .currentTrip(currentTrip)
                .alerts(alerts)
                .build();
    }

    /**
     * Create OwnerSummary from raw values (service layer call)
     */
    public static OwnerSummary buildOwnerSummary(
            Long ownerId,
            String name,
            double rating,
            String kycStatus,
            boolean blocked) {

        return OwnerSummary.builder()
                .ownerId(ownerId)
                .name(name)
                .rating(rating)
                .kycStatus(kycStatus)
                .blocked(blocked)
                .build();
    }

    /**
     * Empty fallback dashboard (Redis miss / error)
     */
    public static OwnerDashboardResponse emptyDashboard(Long ownerId) {

        return OwnerDashboardResponse.builder()
                .owner(
                        OwnerSummary.builder()
                                .ownerId(ownerId)
                                .build())
                .bookingStats(BookingStats.builder().build())
                .vehicleStats(VehicleStats.builder().build())
                .earnings(Earnings.builder().build())
                .currentTrip(null)
                .alerts(OwnerAlerts.builder().build())
                .build();
    }
}
