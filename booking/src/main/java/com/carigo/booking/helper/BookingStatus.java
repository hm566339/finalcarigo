package com.carigo.booking.helper;

public enum BookingStatus {
    PENDING, // request raised
    APPROVED, // owner accepted
    REJECTED, // owner rejected
    PAID, // payment done
    ONGOING, // trip started
    COMPLETED, // trip finished
    CANCELLED
}
