package com.carigo.vehicle.service;

import java.util.Map;

/**
 * Adapter interface for external vehicle verification APIs
 * (Parivahan / Fastag / Insurance API etc.)
 */
public interface ExternalVehicleClient {

    /**
     * Fetch official vehicle data using vehicle number.
     */
    Map<String, Object> fetchByNumber(String vehicleNumber);
}
