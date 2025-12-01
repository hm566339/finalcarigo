package com.hms.profile.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RenterRequestDTO {

    private LocalDate dob;

    @NotBlank
    private String phone;

    private String address;

    @NotBlank
    private String aadhaarNumber;

    private String drivingLicenseNumber;
}
