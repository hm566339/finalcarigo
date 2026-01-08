package com.carigo.vehicle.repository;

import com.carigo.vehicle.model.VehicleMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VehicleMaintenanceRepository extends JpaRepository<VehicleMaintenance, Long> {

        @Query("SELECT m FROM VehicleMaintenance m " +
                        "WHERE m.vehicleId = :vehicleId " +
                        "AND (m.startDate < :end AND m.endDate > :start)")
        List<VehicleMaintenance> findOverlap(String vehicleId,
                        LocalDateTime start,
                        LocalDateTime end);
}
