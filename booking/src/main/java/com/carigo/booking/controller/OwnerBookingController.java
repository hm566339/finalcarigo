package com.carigo.booking.controller;

import com.carigo.booking.dto.BookingResponseDTO;
import com.carigo.booking.entity.BookingStats;
import com.carigo.booking.entity.CurrentTrip;
import com.carigo.booking.service.OwnerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/bookings")
@RequiredArgsConstructor
public class OwnerBookingController {

    private final OwnerBookingService ownerBookingService;

    // ================= OWNER ACTIONS =================

    // Approve booking
    @PutMapping("/{bookingId}/approve")
    public BookingResponseDTO approveBooking(
            @PathVariable Long bookingId) {
        return ownerBookingService.approveBooking(bookingId);
    }

    // Reject booking
    @PutMapping("/{bookingId}/reject")
    public BookingResponseDTO rejectBooking(
            @PathVariable Long bookingId) {
        return ownerBookingService.rejectBooking(bookingId);
    }

    // Start trip
    @PutMapping("/{bookingId}/start")
    public BookingResponseDTO startTrip(
            @PathVariable Long bookingId) {
        return ownerBookingService.startTrip(bookingId);
    }

    // End trip
    @PutMapping("/{bookingId}/end")
    public BookingResponseDTO endTrip(
            @PathVariable Long bookingId) {
        return ownerBookingService.endTrip(bookingId);
    }

    // ================= OWNER DASHBOARD =================

    // Total bookings for owner
    @GetMapping("/count")
    public Long countOwnerBookings(
            @RequestParam Long ownerId) {
        return ownerBookingService.countOwnerBookings(ownerId);
    }

    // Ongoing bookings for owner
    @GetMapping("/ongoing")
    public List<BookingResponseDTO> getOngoingBookings(
            @RequestParam Long ownerId) {
        return ownerBookingService.getOngoingBookings(ownerId);
    }

    // ================= OWNER DASHBOARD =================

    // Booking statistics (dashboard)
    @GetMapping("/stats")
    public BookingStats bookingStats(@RequestParam Long ownerId) {
        return ownerBookingService.getBookingStats(ownerId);
    }

    @GetMapping("/current-trip")
    public CurrentTrip currentTrip(@RequestParam Long ownerId) {
        return ownerBookingService.getCurrentTrip(ownerId);
    }

    @GetMapping("/disputes/count")
    public long disputeCount(@RequestParam Long ownerId) {
        return ownerBookingService.countDisputes(ownerId);
    }

}
