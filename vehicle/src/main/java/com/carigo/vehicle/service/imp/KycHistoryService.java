package com.carigo.vehicle.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.carigo.vehicle.model.KycHistory;
import com.carigo.vehicle.repository.KycHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KycHistoryService {

    private final KycHistoryRepository repo;

    public List<KycHistory> getHistory(String vehicleId) {
        return repo.findByVehicleIdOrderByCreatedAtDesc(vehicleId);
    }
}
