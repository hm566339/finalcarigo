package com.carigo.drivinglicense.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carigo.drivinglicense.model.DrivingLicenseEntity;

import java.util.Optional;

public interface DrivingLicenseRepository extends JpaRepository<DrivingLicenseEntity, String> {
    Optional<DrivingLicenseEntity> findByDlNumber(String dlNumber);

    Optional<DrivingLicenseEntity> findByUserId(Long id);

}
