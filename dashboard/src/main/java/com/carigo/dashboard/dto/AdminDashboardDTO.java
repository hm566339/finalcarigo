package com.carigo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.BookingTrends;
import com.carigo.dashboard.entity.KycStats;
import com.carigo.dashboard.entity.PlatformStats;
import com.carigo.dashboard.entity.Revenue;
import com.carigo.dashboard.entity.VehicleStats;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO implements Serializable {

    private PlatformStats platform;
    private VehicleStats vehicles;
    private BookingStats bookings;
    private Revenue revenue;
    private KycStats kyc;
    private List<String> alerts;
    private BookingTrends bookingTrends;

}
