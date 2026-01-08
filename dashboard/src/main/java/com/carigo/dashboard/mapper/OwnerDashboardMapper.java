package com.carigo.dashboard.mapper;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

import com.carigo.dashboard.dto.BookingStats;
import com.carigo.dashboard.dto.Earnings;
import com.carigo.dashboard.dto.OwnerDashboardDTO;

@UtilityClass
public class OwnerDashboardMapper {

    public OwnerDashboardDTO map(
            Long ownerId,
            Map<String, Object> ownerProfile,
            List<?> vehicles,
            BookingStats bookingStats,
            Earnings earnings,
            double rating,
            int reviews,
            List<String> alerts) {

        return OwnerDashboardDTO.builder()
                .ownerId(ownerId)
                .ownerName((String) ownerProfile.get("name"))
                .kycStatus(String.valueOf(ownerProfile.get("kycStatus")))

                .totalCars(vehicles.size())
                .activeCars((int) vehicles.stream()
                        .filter(v -> "ACTIVE".equals(getField(v, "status")))
                        .count())
                .inactiveCars((int) vehicles.stream()
                        .filter(v -> !"ACTIVE".equals(getField(v, "status")))
                        .count())

                .bookingStats(bookingStats)
                .earnings(earnings)

                .averageRating(rating)
                .totalReviews(reviews)

                .alerts(alerts)
                .build();
    }

    // Safe reflection-based getter (Feign response friendly)
    private static Object getField(Object obj, String field) {
        try {
            return obj.getClass()
                    .getMethod("get" + capitalize(field))
                    .invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
