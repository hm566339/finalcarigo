package com.hms.profile.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RenterResponseDTO {

    private Long id;

    private String name;
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;

    private String aadhaarNumber;
    private String drivingLicenseNumber;

    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;
    private String selfieUrl;

    private Double rating;
    private Integer totalTrips;

    private String kycStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
