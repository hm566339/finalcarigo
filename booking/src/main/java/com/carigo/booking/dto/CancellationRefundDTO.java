package com.carigo.booking.dto;

import lombok.Data;

@Data
public class CancellationRefundDTO {
    private Double refundAmount;
    private String message;
}
