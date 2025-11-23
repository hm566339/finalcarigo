package com.hms.profile.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kyc_consent")
public class KycConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profileId;

    @Column(length = 20)
    private String profileType; // OWNER / RENTER

    private Boolean consentGiven = false;

    private String purpose; // "KYC_VERIFICATION"

    private LocalDateTime consentedAt = LocalDateTime.now();

    private LocalDateTime expiresAt;
}
