package com.carigo.pament.client;

import org.springframework.stereotype.Component;

import com.carigo.pament.dto.BookingDTO;

@Component
public class BookingClientFallback implements BookingClient {

    @Override
    public BookingDTO getBookingById(Long id) {
        BookingDTO fallback = new BookingDTO();
        fallback.setId(id);
        fallback.setStatus("UNAVAILABLE");
        return fallback;
    }
}
