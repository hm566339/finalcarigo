package com.carigo.booking.dto;

import lombok.Data;

@Data
public class ReviewRequestDto {
    private Long renterId;
    private Integer rating; // 1-5
    private String comment;
}
