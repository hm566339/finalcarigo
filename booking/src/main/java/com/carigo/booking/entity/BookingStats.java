package com.carigo.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingStats implements Serializable {

    private long total;
    private long today;
    private long ongoing;
    private long upcoming;
    private long completed;
    private long cancelled;
    private long disputed;
}
