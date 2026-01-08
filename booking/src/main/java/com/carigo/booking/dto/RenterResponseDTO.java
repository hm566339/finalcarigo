package com.carigo.booking.dto;

import lombok.Data;

@Data
public class RenterResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
