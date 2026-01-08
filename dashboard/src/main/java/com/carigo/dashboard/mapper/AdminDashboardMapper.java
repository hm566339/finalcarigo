package com.carigo.dashboard.mapper;

import lombok.experimental.UtilityClass;

import java.util.List;

import com.carigo.dashboard.dto.AdminDashboardDTO;
import com.carigo.dashboard.dto.BookingStats;
import com.carigo.dashboard.dto.BookingTrends;
import com.carigo.dashboard.dto.KycStats;
import com.carigo.dashboard.dto.PlatformStats;
import com.carigo.dashboard.dto.VehicleStats;

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
