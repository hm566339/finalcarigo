package com.carigo.pament.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDTO {

    private Long id;
    private Long renterId;
    private Long ownerId;
    private String vehicleId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private double price;
    private String status;
}
