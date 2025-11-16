package com.carigo.vehicle.dto;

import lombok.Data;

@Data
public class VerifyRequest {

    private String externalVehicleNumber;

    private String externalChassisNumber;

    private String externalEngineNumber;

    private String externalOwnerName;

    // optional: full raw API JSON for debugging / storing
    private Object rawApiResponse;
}
