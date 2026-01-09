package com.carigo.booking.controller;

import com.carigo.booking.dto.*;
import com.carigo.booking.service.RenterBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/renter/bookings")
@RequiredArgsConstructor
public class RenterBookingController {

    private final RenterBookingService renterBookingService;

    // ================= RENTER ACTIONS =================

    // Create booking
    @PostMapping
    public BookingResponseDTO createBooking(
            @RequestBody BookingRequestDTO dto) {
        return renterBookingService.createBooking(dto);
    }

    // Payment success
    @PutMapping("/{bookingId}/payment-success")
    public BookingResponseDTO paymentSuccess(
            @PathVariable Long bookingId) {
        return renterBookingService.paymentSuccess(bookingId);
    }

    // Cancel booking
    @PutMapping("/{bookingId}/cancel")
    public BookingResponseDTO cancelBooking(
            @PathVariable Long bookingId) {
        return renterBookingService.cancelBooking(bookingId);
    }

    // Submit review
    @PostMapping("/{bookingId}/review")
    public void submitReview(
            @PathVariable Long bookingId,
            @RequestBody ReviewRequestDto dto) {
        renterBookingService.submitReview(bookingId, dto);
    }

    // ================= RENTER DASHBOARD =================

    // Total bookings count for renter
    @GetMapping("/count")
    public Long countTotalBookings(
            @RequestParam Long renterId) {
        return renterBookingService.countTotalBookings(renterId);
    }

    // Active bookings for renter
    @GetMapping("/active")
    public List<BookingResponseDTO> getActiveBookings(
            @RequestParam Long renterId) {
        return renterBookingService.getActiveBookings(renterId);
    }

    @GetMapping("/completed")
    public long completedBookings(@RequestParam Long renterId) {
        return renterBookingService.countCompletedBookings(renterId);
    }

    @GetMapping("/current")
    public BookingResponseDTO currentTrip(@RequestParam Long renterId) {
        return renterBookingService.getCurrentTrip(renterId);
    }

}
