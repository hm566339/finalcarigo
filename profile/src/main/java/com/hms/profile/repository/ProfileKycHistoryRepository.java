package com.hms.profile.repository;

import com.hms.profile.model.ProfileKycHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileKycHistoryRepository extends JpaRepository<ProfileKycHistory, Long> {
    List<ProfileKycHistory> findByProfileIdOrderByCreatedAtDesc(Long profileId);

    List<ProfileKycHistory> findByProfileIdAndProfileTypeOrderByCreatedAtDesc(
            Long profileId,
            String profileType);

}
