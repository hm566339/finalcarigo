package com.carigo.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAndVehicleVerify {

    private Long ownerId;
    private String vehicleId;
    private Double preDay;

}
