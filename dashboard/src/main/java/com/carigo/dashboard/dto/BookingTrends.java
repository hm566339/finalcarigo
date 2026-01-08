package com.carigo.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingTrends {
    private List<String> labels;
    private List<Long> total;
    private List<Long> completed;
    private List<Long> cancelled;
}
