package com.hms.profile.repository;

import com.hms.profile.model.KycJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KycJobRepository extends JpaRepository<KycJob, Long> {
    List<KycJob> findByProfileIdOrderByCreatedAtDesc(Long profileId);
}
