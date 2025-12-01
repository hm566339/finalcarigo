package com.carigo.drivinglicense.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.carigo.drivinglicense.helper.KycStatus;

@Data
@NoArgsConstructor
@Entity
@Table(name = "driving_license", indexes = {
        @Index(name = "idx_dl_number", columnList = "dl_number"),
        @Index(name = "idx_user_dl", columnList = "user_id, dl_number")
})
public class DrivingLicenseEntity {

    @Id
    @Column(name = "dl_id", nullable = false, updatable = false, length = 50)
    private String dlId = UUID.randomUUID().toString();

    @Column(name = "user_id", nullable = false, length = 50)
    private Long userId;

    @Column(name = "dl_number", nullable = false, unique = true, length = 30)
    private String dlNumber;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "father_name", length = 100)
    private String fatherName;

    @Column(name = "dob", length = 20)
    private String dateOfBirth;

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "issue_date", length = 20)
    private String issueDate;

    @Column(name = "expiry_date", length = 20)
    private String expiryDate;

    @Column(name = "vehicle_classes", length = 200)
    private String vehicleClasses; // LMV, MCWG, etc.

    @Column(length = 1000)
    private String frontImageUrl;

    @Column(length = 1000)
    private String backImageUrl;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus = KycStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
