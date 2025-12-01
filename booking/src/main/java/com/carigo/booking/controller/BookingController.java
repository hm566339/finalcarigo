package com.carigo.booking.controller;

import com.carigo.booking.dto.BookingRequestDTO;
import com.carigo.booking.dto.BookingResponseDTO;
import com.carigo.booking.service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // ======================= CREATE BOOKING ==========================
    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO requestDTO) {

        BookingResponseDTO response = bookingService.createBooking(requestDTO);
        return ResponseEntity.ok(response);
    }

    // ======================== APPROVE BOOKING ========================
    @PutMapping("/{id}/approve")
    public ResponseEntity<BookingResponseDTO> approveBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.approveBooking(id));
    }

    // ======================== REJECT BOOKING =========================
    @PutMapping("/{id}/reject")
    public ResponseEntity<BookingResponseDTO> rejectBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    // ====================== PAYMENT SUCCESS ==========================
    @PutMapping("/{id}/payment-success")
    public ResponseEntity<BookingResponseDTO> paymentSuccess(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.paymentSuccess(id));
    }

    // ======================== TRIP START =============================
    @PutMapping("/{id}/start-trip")
    public ResponseEntity<BookingResponseDTO> startTrip(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.startTrip(id));
    }

    // ======================== TRIP END ===============================
    @PutMapping("/{id}/end-trip")
    public ResponseEntity<BookingResponseDTO> endTrip(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.endTrip(id));
    }

    // ======================== CANCEL BOOKING =========================
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }
}
