package com.carigo.booking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceQuoteDto {

    private BigDecimal baseAmount;
    private BigDecimal finalAmount;

    private BigDecimal discount;
    private String promoCode;
    private String promoMessage;
}
