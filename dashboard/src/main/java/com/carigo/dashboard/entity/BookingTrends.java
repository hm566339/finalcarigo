package com.carigo.dashboard.entity;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingTrends implements Serializable {

    private List<String> labels;
    private List<Long> total;
    private List<Long> completed;
    private List<Long> cancelled;
}
