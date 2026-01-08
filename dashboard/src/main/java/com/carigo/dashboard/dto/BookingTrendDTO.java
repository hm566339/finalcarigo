package com.carigo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingTrendDTO {
    private String date;
    private long total;
    private long completed;
    private long cancelled;
}
