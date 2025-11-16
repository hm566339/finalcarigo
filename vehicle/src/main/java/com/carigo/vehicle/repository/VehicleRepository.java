package com.carigo.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carigo.vehicle.model.VehicleEntity;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {

    Optional<VehicleEntity> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber); // ✔ faster validation
}
