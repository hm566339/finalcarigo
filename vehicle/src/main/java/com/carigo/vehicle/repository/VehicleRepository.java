package com.carigo.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carigo.vehicle.model.VehicleEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {

    Optional<VehicleEntity> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    List<VehicleEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId); // ✔ correct method
}
