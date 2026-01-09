package com.carigo.dashboard.entity;

import lombok.Data;

@Data
public class RenterResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String kycStatus;
    private Double rating;
}
