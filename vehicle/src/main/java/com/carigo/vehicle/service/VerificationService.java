package com.carigo.vehicle.service;

import org.springframework.stereotype.Service;

import com.carigo.vehicle.model.KycHistory;
import com.carigo.vehicle.model.VehicleEntity;
import com.carigo.vehicle.repository.KycHistoryRepository;

import java.util.Map;

@Service
public class VerificationService {

    private final ExternalVehicleClient externalVehicleClient;
    private final KycHistoryRepository historyRepository;

    public VerificationService(ExternalVehicleClient externalVehicleClient,
            KycHistoryRepository historyRepository) {
        this.externalVehicleClient = externalVehicleClient;
        this.historyRepository = historyRepository;
    }

    // Fetch data from external API
    public Map<String, Object> fetchExternalData(String vehicleNumber) {
        return externalVehicleClient.fetchByNumber(vehicleNumber);
    }

    // Main KYC verification logic
    public boolean matchWithExternal(VehicleEntity local, Object externalApiResponse) {

        if (externalApiResponse == null || !(externalApiResponse instanceof Map)) {
            saveHistory(local, "FAILED", "Invalid or empty response from external API");
            return false;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) externalApiResponse;

        String extNumber = (String) data.get("number");
        String extChassis = (String) data.get("chassis");
        String extEngine = (String) data.get("engine");
        String extOwner = (String) data.get("owner");

        boolean chassisMatch = extChassis != null
                && local.getChassisNumber() != null
                && extChassis.equalsIgnoreCase(local.getChassisNumber());

        boolean engineMatch = extEngine != null
                && local.getEngineNumber() != null
                && extEngine.equalsIgnoreCase(local.getEngineNumber());

        boolean ownerMatch = extOwner != null
                && local.getOwnerName() != null
                && extOwner.equalsIgnoreCase(local.getOwnerName());

        boolean numberMatch = extNumber != null
                && extNumber.equalsIgnoreCase(local.getVehicleNumber());

        // ---------------------------------------------
        // PRIORITY BASED REAL-WORLD KYC VERIFICATION
        // ---------------------------------------------

        // 1. Chassis + Engine Strong Match
        if (chassisMatch && engineMatch) {
            saveHistory(local, "VERIFIED", "Chassis & Engine both matched");
            return true;
        }

        // 2. Only Chassis Match
        if (chassisMatch) {
            saveHistory(local, "VERIFIED", "Chassis matched");
            return true;
        }

        // 3. Only Engine Match
        if (engineMatch) {
            saveHistory(local, "VERIFIED", "Engine matched");
            return true;
        }

        // 4. Owner + Number match (high confidence)
        if (ownerMatch && numberMatch) {
            saveHistory(local, "MANUAL_CHECK", "Owner & Number matched");
            return false; // manual approval required
        }

        // 5. Only number matched → NOT VERIFIED, manual review
        if (numberMatch) {
            saveHistory(local, "MANUAL_CHECK", "Only vehicle number matched — weak evidence");
            return false;
        }

        // 6. Nothing matched
        saveHistory(local, "FAILED", "No fields matched");
        return false;
    }

    // Audit history save
    private void saveHistory(VehicleEntity vehicle, String status, String detail) {
        KycHistory h = new KycHistory();
        h.setVehicleId(vehicle.getVehicleId());
        h.setAction(status);
        h.setDetail(detail);
        historyRepository.save(h);
    }
}
