package com.carigo.booking.controller;

import com.carigo.booking.dto.BookingTrendDTO;
import com.carigo.booking.service.AdminBookingService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    // ================= TOTAL BOOKINGS =================
    // GET /admin/bookings/count
    @GetMapping("/count")
    public long countAll() {
        return adminBookingService.countAllBookings();
    }

    // ================= TODAY BOOKINGS =================
    // GET /admin/bookings/count/today
    @GetMapping("/count/today")
    public long countToday() {
        return adminBookingService.countTodayBookings();
    }

    // ================= UPCOMING BOOKINGS =================
    // ✅ FIXED
    // GET /admin/bookings/count/upcoming
    @GetMapping("/count/upcoming")
    public long countUpcoming() {
        return adminBookingService.countUpcomingBookings();
    }

    // ================= ONGOING BOOKINGS =================
    // GET /admin/bookings/count/ongoing
    @GetMapping("/count/ongoing")
    public long countOngoing() {
        return adminBookingService.countOngoingBookings();
    }

    // ================= COMPLETED BOOKINGS =================
    // GET /admin/bookings/count/completed
    @GetMapping("/count/completed")
    public long countCompleted() {
        return adminBookingService.countCompletedBookings();
    }

    // ================= CANCELLED BOOKINGS =================
    // GET /admin/bookings/count/cancelled
    @GetMapping("/count/cancelled")
    public long countCancelled() {
        return adminBookingService.countCancelledBookings();
    }

    // ================= PAYMENT PENDING =================
    // GET /admin/bookings/count/payment-pending
    @GetMapping("/count/payment-pending")
    public long countPaymentPending() {
        return adminBookingService.countPaymentPendingBookings();
    }

    // ================= DISPUTED BOOKINGS =================
    // GET /admin/bookings/count/disputed
    @GetMapping("/count/disputed")
    public long countDisputed() {
        return adminBookingService.countDisputedBookings();
    }

    // ================= STALE DISPUTES =================
    // GET /admin/bookings/disputes/stale
    @GetMapping("/disputes/stale")
    public long staleDisputes() {
        return adminBookingService.countStaleDisputes();
    }

    @GetMapping("/booking-trends")
    public List<BookingTrendDTO> bookingTrends() {
        return adminBookingService.getLast7DaysTrends();
    }

}
