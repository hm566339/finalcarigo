package com.carigo.booking.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TripExtensionRequest {
    private LocalDateTime newEndDate;
}