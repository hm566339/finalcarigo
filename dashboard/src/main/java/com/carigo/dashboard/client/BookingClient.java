package com.carigo.dashboard.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.carigo.dashboard.dto.BookingTrendDTO;
import com.carigo.dashboard.dto.BookingTrends;

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

}
