package com.carigo.vehicle.dto;

import lombok.Data;

@Data
public class UserAndVehicleVerify {

    private Long ownerId;
    private String vehicleId;
    private Double preDay;

}
