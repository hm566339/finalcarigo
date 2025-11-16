package com.carigo.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carigo.vehicle.model.KycHistory;

import java.util.List;
import java.util.Optional;

public interface KycHistoryRepository extends JpaRepository<KycHistory, Long> {

    List<KycHistory> findByVehicleIdOrderByCreatedAtDesc(String vehicleId);

    Optional<KycHistory> findTop1ByVehicleIdOrderByCreatedAtDesc(String vehicleId); // ✔ latest KYC event

    List<KycHistory> findByVehicleIdAndActionOrderByCreatedAtDesc(String vehicleId, String action); // ✔ filter by
                                                                                                    // action
}
