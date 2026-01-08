package com.carigo.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.helper.VehicleStatus;
import com.carigo.vehicle.model.VehicleEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {

    Optional<VehicleEntity> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    List<VehicleEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId); // ✔ correct method

    void deleteByVehicleId(String vehicleId);

    long countByStatus(VehicleStatus status);

    long countByKycStatus(KycStatus status);

    long countByInsuranceExpiryDateBefore(LocalDate date);

}
