package com.hms.profile.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "renter", indexes = @Index(name = "idx_renter_email", columnList = "email"))
public class Renter {

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

    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;
    private String selfieUrl;

    // RENTER-SPECIFIC FIELDS
    private Double rating = 0.0;
    private Integer totalTrips = 0;

    private String kycStatus = "PENDING"; // PENDING / VERIFIED / REJECTED

    // AUDIT FIELDS
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
