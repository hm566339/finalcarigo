package com.carigo.vehicle.service.imp;

import com.carigo.vehicle.model.VehicleMaintenance;
import com.carigo.vehicle.repository.VehicleMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final VehicleMaintenanceRepository repo;

    public boolean isInMaintenance(String vehicleId, LocalDateTime start, LocalDateTime end) {
        return !repo.findOverlap(vehicleId, start, end).isEmpty();
    }

    public VehicleMaintenance addMaintenance(VehicleMaintenance m) {
        return repo.save(m);
    }
}
