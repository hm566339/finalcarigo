package com.carigo.dashboard.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {

    private Long id;

    private Long renterId;
    private Long ownerId;
    private String vehicleId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Double price;
    private Double securityDeposit;

    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
