package com.hms.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.profile.model.CarOwner;

public interface CarOwnerRepository extends JpaRepository<CarOwner, Long> {

    List<CarOwner> findByNameContainingIgnoreCase(String name);

    Optional<CarOwner> findByEmail(String email);

    // boolean existsByVehicleNumber(String vehicleNumber);

    long countByKycStatus(String status);

}
