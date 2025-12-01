package com.carigo.booking.repository;

import com.carigo.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // find bookings that overlap with given date range
    @Query("SELECT b FROM Booking b " +
            "WHERE b.vehicleId = :vehicleId " +
            "AND b.status NOT IN ('CANCELLED', 'REJECTED', 'COMPLETED') " +
            "AND (b.startDate < :end AND b.endDate > :start)")
    List<Booking> findOverlappingBookings(String vehicleId, LocalDateTime start, LocalDateTime end);
}
