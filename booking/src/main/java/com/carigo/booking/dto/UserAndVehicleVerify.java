package com.carigo.booking.dto;

import lombok.Data;

@Data
public class UserAndVehicleVerify {

    private Long ownerId;
    private String vehicleId;
    private Double preDay;

}
