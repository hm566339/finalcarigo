package com.hms.profile.repository;

import com.hms.profile.model.KycConsent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycConsentRepository extends JpaRepository<KycConsent, Long> {
    Optional<KycConsent> findTopByProfileIdOrderByConsentedAtDesc(Long profileId);
}
