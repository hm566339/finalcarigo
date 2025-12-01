package com.hms.profile.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "profile_kyc_history", indexes = {
        @Index(name = "idx_profile", columnList = "profile_id")
})
public class ProfileKycHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // owner or renter
    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(nullable = false, length = 20)
    private String profileType; // OWNER / RENTER

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private com.hms.profile.helper.KycStatus action; // PENDING / VERIFIED / FAILED / MANUAL_CHECK

    @Column(length = 1000)
    private String detail;

    private LocalDateTime createdAt = LocalDateTime.now();
}
