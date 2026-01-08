package com.hms.profile.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hms.profile.dto.BloodGroup;
import com.hms.profile.helper.Status;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car_owner", indexes = @Index(name = "idx_owner_email", columnList = "email"))
public class CarOwner {

    @Id
    private Long id;

    // BASIC PROFILE
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate dob;
    private String phone;
    private String address;

    // KYC DOCUMENTS
    @Column(unique = true)
    private String aadhaarNumber;

    @Column(unique = true)
    private String drivingLicenseNumber;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;
    private String selfieUrl;

    // OWNER-SPECIFIC FIELDS
    private Double walletBalance = 0.0;
    private Double totalEarnings = 0.0;

    private String bankAccountNumber;
    private String ifscCode;
    private String accountHolderName;

    private Double rating = 0.0;
    private Integer totalTripsCompleted = 0;

    // KYC STATUS
    private String kycStatus = "PENDING"; // PENDING / VERIFIED / REJECTED

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.ACTIVE;

    // AUDIT FIELDS
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
