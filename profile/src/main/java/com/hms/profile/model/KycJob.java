package com.hms.profile.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kyc_job", indexes = {
        @Index(name = "idx_job_profile", columnList = "profile_id")
})
public class KycJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profileId;

    @Column(length = 20)
    private String profileType; // OWNER / RENTER

    @Column(length = 20)
    private String status; // QUEUED / RUNNING / SUCCESS / FAILED

    private String detail;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
