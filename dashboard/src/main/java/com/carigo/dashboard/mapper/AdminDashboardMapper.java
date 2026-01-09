package com.carigo.dashboard.mapper;

import lombok.experimental.UtilityClass;

import java.util.List;

import com.carigo.dashboard.dto.AdminDashboardDTO;
import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.BookingTrends;
import com.carigo.dashboard.entity.KycStats;
import com.carigo.dashboard.entity.PlatformStats;
import com.carigo.dashboard.entity.VehicleStats;

@UtilityClass
public class AdminDashboardMapper {

    public AdminDashboardDTO map(
            PlatformStats platform,
            VehicleStats vehicles,
            BookingStats bookings,
            BookingTrends bookingTrends, // ✅ 4th
            KycStats kyc, // ✅ 5th
            List<String> alerts) {

        return AdminDashboardDTO.builder()
                .platform(platform)
                .vehicles(vehicles)
                .bookings(bookings)
                .bookingTrends(bookingTrends)
                .kyc(kyc)
                .alerts(alerts)
                .build();
    }
}
