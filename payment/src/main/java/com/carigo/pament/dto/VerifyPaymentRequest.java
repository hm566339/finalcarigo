package com.carigo.pament.dto;

import lombok.Data;

@Data
public class VerifyPaymentRequest {

    private String orderId; // From Razorpay
    private String paymentId; // From Razorpay
    private String signature; // Razorpay signature

    private Long renterId;
    private Long ownerId;
    private Long bookingId;

    private double amount; // Amount in INR
    private String secret; // Razorpay Secret for signature verification

}
