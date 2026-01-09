package com.carigo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.CurrentTrip;
import com.carigo.dashboard.entity.Spending;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenterDashboardDTO implements Serializable {

    private Long renterId;
    private String renterName;
    private String kycStatus;

    // Bookings
    private BookingStats bookingStats;

    // Current Trip (nullable)
    private CurrentTrip currentTrip;

    // Spending
    private Spending spending;

    // Ratings
    private double averageRating;

    // Alerts
    private List<String> alerts;

    private long totalBookings;
}
