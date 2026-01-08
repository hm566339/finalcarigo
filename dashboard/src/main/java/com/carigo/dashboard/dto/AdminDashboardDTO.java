package com.carigo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
