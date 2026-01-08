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
public class OwnerDashboardDTO implements Serializable {

    private Long ownerId;
    private String ownerName;
    private String kycStatus;

    // Vehicles
    private int totalCars;
    private int activeCars;
    private int inactiveCars;

    // Bookings
    private BookingStats bookingStats;

    // Earnings
    private Earnings earnings;

    // Ratings
    private double averageRating;
    private int totalReviews;

    // Alerts / Notifications
    private List<String> alerts;
}
