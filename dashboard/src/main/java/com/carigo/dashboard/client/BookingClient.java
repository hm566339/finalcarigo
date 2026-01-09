package com.carigo.dashboard.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carigo.dashboard.entity.BookingResponseDTO;
import com.carigo.dashboard.entity.BookingStats;
import com.carigo.dashboard.entity.BookingTrendDTO;
import com.carigo.dashboard.entity.BookingTrends;
import com.carigo.dashboard.entity.CurrentTrip;

@FeignClient(name = "booking-service", url = "http://localhost:8084")
public interface BookingClient {

    @GetMapping("/admin/bookings/count")
    long countAll();

    @GetMapping("/admin/bookings/count/today")
    long countToday();

    @GetMapping("/admin/bookings/count/ongoing")
    long countOngoing();

    @GetMapping("/admin/bookings/count/completed")
    long countCompleted();

    @GetMapping("/admin/bookings/count/cancelled")
    long countCancelled();

    @GetMapping("/admin/bookings/count/disputed")
    long countDisputed();

    @GetMapping("/admin/bookings/count/payment-pending")
    long countPaymentPending();

    @GetMapping("/admin/bookings/disputes/stale")
    long staleDisputes();

    @GetMapping("/admin/bookings/count/upcoming")
    long countUpcoming();

    @GetMapping("/admin/bookings/booking-trends")
    List<BookingTrendDTO> bookingTrends();

    // 📊 Booking stats
    @GetMapping("/owner/bookings/stats")
    BookingStats bookingStats(@RequestParam("ownerId") Long ownerId);

    // 🚗 Current ongoing trip
    @GetMapping("/owner/bookings/current-trip")
    CurrentTrip currentTrip(@RequestParam("ownerId") Long ownerId);

    // ⚠️ Disputes count (optional)
    @GetMapping("/owner/bookings/disputes/count")
    long disputeCount(@RequestParam("ownerId") Long ownerId);

    // 📊 total bookings
    @GetMapping("/renter/bookings/count")
    long countTotal(@RequestParam Long renterId);

    // 🔥 active bookings (PENDING, APPROVED, PAID, ONGOING)
    @GetMapping("/renter/bookings/active")
    List<BookingResponseDTO> activeBookings(@RequestParam Long renterId);

    // 📅 completed bookings
    @GetMapping("/renter/bookings/completed")
    long completedBookings(@RequestParam Long renterId);

    // 🚗 current ongoing trip
    @GetMapping("/renter/bookings/current")
    BookingResponseDTO currentRenterTrip(@RequestParam Long renterId);
}
