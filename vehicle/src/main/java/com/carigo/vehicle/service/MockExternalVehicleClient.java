package com.carigo.vehicle.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.carigo.vehicle.client.ExternalVehicleClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile({ "dev", "default" }) // <-- MOST IMPORTANT
public class MockExternalVehicleClient implements ExternalVehicleClient {

    @Override
    public Map<String, Object> fetchByNumber(String vehicleNumber) {

        Map<String, Object> data = new HashMap<>();
        data.put("number", vehicleNumber);
        data.put("chassis", "MOCK-CHASSIS-12345");
        data.put("engine", "MOCK-ENGINE-98765");
        data.put("owner", "MOCK OWNER");
        data.put("source", "mock");

        return data;
    }
}
