package com.carigo.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequestDTO {

    private Long renterId;
    private Long ownerId;
    private String vehicleId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Double price;
    private Double securityDeposit;
}
