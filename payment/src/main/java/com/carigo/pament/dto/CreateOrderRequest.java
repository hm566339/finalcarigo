package com.carigo.pament.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private Long bookingId;
    private Long renterId;
    private Long ownerId;

    private double amount; // Amount in INR (not paise)
    private String currency = "INR"; // Default

}
