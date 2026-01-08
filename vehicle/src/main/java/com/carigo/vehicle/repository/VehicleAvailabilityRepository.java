package com.carigo.vehicle.repository;

import com.carigo.vehicle.model.VehicleAvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VehicleAvailabilityRepository extends JpaRepository<VehicleAvailabilitySlot, Long> {

        List<VehicleAvailabilitySlot> findByVehicleIdAndStartTimeBetween(
                        String vehicleId,
                        LocalDateTime from,
                        LocalDateTime to);

        List<VehicleAvailabilitySlot> findByVehicleIdAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
                        String vehicleId,
                        LocalDateTime from,
                        LocalDateTime to);

        void deleteByVehicleId(String vehicleId);
}
