package com.carigo.dashboard.entity;

import lombok.Data;
import java.time.LocalDateTime;

import com.carigo.dashboard.helper.KycStatus;

@Data
public class DrivingLicenseDto {

    private String dlId;
    private Long userId; // FIXED: must match entity
    private String dlNumber;
    private String fullName;
    private String fatherName;
    private String dateOfBirth;
    private String bloodGroup;
    private String address;
    private String issueDate;
    private String expiryDate;
    private String vehicleClasses;
    private String frontImageUrl;
    private String backImageUrl;
    private KycStatus kycStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
