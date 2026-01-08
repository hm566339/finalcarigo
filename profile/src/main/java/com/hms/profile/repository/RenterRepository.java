package com.hms.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.profile.model.Renter;

public interface RenterRepository extends JpaRepository<Renter, Long> {

    Optional<Renter> findByEmail(String email);

    Optional<Renter> findByNameContainingIgnoreCase(String name);

    Boolean existsByDrivingLicenseNumber(String dl);

    long countByKycStatus(String status);

}
