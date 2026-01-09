package com.carigo.dashboard.dto;

import java.io.Serializable;

import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.CurrentTrip;
import com.carigo.dashboard.entity.Earnings;
import com.carigo.dashboard.entity.OwnerAlerts;
import com.carigo.dashboard.entity.OwnerSummary;
import com.carigo.dashboard.entity.VehicleStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDashboardResponse implements Serializable {

    private OwnerSummary owner;
    private BookingStats bookingStats;
    private VehicleStats vehicleStats;
    private Earnings earnings;
    private CurrentTrip currentTrip;
    private OwnerAlerts alerts;
}
