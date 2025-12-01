package com.hms.profile.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CarOwnerRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    private LocalDate dob;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String address;

    @NotBlank(message = "Aadhaar number is required")
    private String aadhaarNumber;

    private String drivingLicenseNumber;

    private String bloodGroup; // Enum as String (A+, O-, etc.)
}
