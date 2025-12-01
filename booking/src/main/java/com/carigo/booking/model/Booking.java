package com.carigo.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.carigo.booking.helper.BookingStatus;

@Data
@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_renter", columnList = "renter_id"),
        @Index(name = "idx_booking_owner", columnList = "owner_id"),
        @Index(name = "idx_booking_vehicle", columnList = "vehicle_id")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // renter/user who booked the car
    @Column(name = "renter_id", nullable = false)
    private Long renterId;

    // owner of the car
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    // vehicle id
    @Column(name = "vehicle_id", nullable = false, length = 50)
    private String vehicleId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double securityDeposit;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BookingStatus status = BookingStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
