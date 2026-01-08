package com.carigo.drivinglicense.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carigo.drivinglicense.helper.KycStatus;
import com.carigo.drivinglicense.model.DlKycHistory;
import com.carigo.drivinglicense.model.DrivingLicenseEntity;

import java.util.List;
import java.util.Optional;

public interface DrivingLicenseRepository extends JpaRepository<DrivingLicenseEntity, String> {
    Optional<DrivingLicenseEntity> findByDlNumber(String dlNumber);

    Optional<DrivingLicenseEntity> findByUserId(Long id);

    Optional<DrivingLicenseEntity> findByKycStatus(KycStatus status, PageRequest of);

    List<DlKycHistory> findByDlIdOrderByCreatedAtDesc(String dlId);

    @Query("""
                SELECT d FROM DrivingLicenseEntity d
                WHERE d.expiryDate IS NOT NULL
                AND d.expiryDate <= :limitDate
            """)
    List<DrivingLicenseEntity> findExpiringBefore(@Param("limitDate") String limitDate);

}
