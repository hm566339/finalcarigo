package com.carigo.pament.model;

public enum PaymentStatus {
    PENDING, // Order created but payment not done
    SUCCESS, // Payment completed successfully
    FAILED, // Payment failed at Razorpay
    REFUNDED // Amount sent back or transferred to bank
}
