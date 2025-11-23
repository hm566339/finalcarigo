package com.hms.profile.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CarOwnerResponseDTO {

    private Long id;

    private Long userId;
    private String vehicleNumber;
    private String name;
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;

    private String aadhaarNumber;
    private String drivingLicenseNumber;

    private String bloodGroup;

    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;
    private String selfieUrl;

    private Double walletBalance;
    private Double totalEarnings;

    private String bankAccountNumber;
    private String ifscCode;
    private String accountHolderName;

    private Double rating;
    private Integer totalTripsCompleted;

    private String kycStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
