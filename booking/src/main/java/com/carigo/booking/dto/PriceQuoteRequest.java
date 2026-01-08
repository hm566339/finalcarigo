package com.carigo.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PriceQuoteRequest {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String promoCode;
}
