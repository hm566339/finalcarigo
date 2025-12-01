package com.carigo.vehicle.service;

import org.springframework.stereotype.Service;

import com.carigo.vehicle.client.ExternalVehicleClient;
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

    public Map<String, Object> fetchExternalData(String vehicleNumber) {
        return externalVehicleClient.fetchByNumber(vehicleNumber);
    }

    boolean matchWithExternal(VehicleEntity local, Object externalApiResponse) {

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
                && extNumber.equalsIgnoreCase(local.getVehicleId());

        // --------------------------------------
        // PRIORITY KYC MATCHING LOGIC (BEST)
        // --------------------------------------

        if (chassisMatch && engineMatch) {
            saveHistory(local, "VERIFIED", "Chassis & Engine both matched");
            return true;
        }

        if (chassisMatch) {
            saveHistory(local, "VERIFIED", "Chassis matched");
            return true;
        }

        if (engineMatch) {
            saveHistory(local, "VERIFIED", "Engine matched");
            return true;
        }

        if (ownerMatch && numberMatch) {
            saveHistory(local, "MANUAL_CHECK", "Owner & Number matched");
            return false;
        }

        if (numberMatch) {
            saveHistory(local, "MANUAL_CHECK", "Only vehicle number matched — weak evidence");
            return false;
        }

        saveHistory(local, "FAILED", "No fields matched");
        return false;
    }

    private void saveHistory(VehicleEntity vehicle, String status, String detail) {
        KycHistory h = new KycHistory();
        h.setVehicleId(vehicle.getVehicleId());
        h.setAction(status);
        h.setDetail(detail);
        historyRepository.save(h);
    }
}
