package com.carigo.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.carigo.booking.model.Booking;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentTrip implements Serializable {

    private Long bookingId;
    private String vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    /**
     * 🔥 Safe mapper: Booking → CurrentTrip
     */
    public static CurrentTrip fromBooking(Booking booking) {
        if (booking == null) {
            return null;
        }

        return CurrentTrip.builder()
                .bookingId(booking.getId())
                .vehicleId(booking.getVehicleId())
                .startTime(booking.getStartDate())
                .endTime(booking.getEndDate())
                .status(booking.getStatus().name())
                .build();
    }
}
