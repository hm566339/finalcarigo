package com.carigo.pament.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.carigo.pament.config.FeignConfig;
import com.carigo.pament.dto.BookingDTO;

@FeignClient(name = "BOOKING-SERVICE", // Service name from Eureka
        url = "http://booking-service/bookings", configuration = FeignConfig.class, fallback = BookingClientFallback.class)
public interface BookingClient {

    @GetMapping("/{id}")
    BookingDTO getBookingById(@PathVariable("id") Long id);
}
