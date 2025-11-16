package com.carigo.vehicle.dto;

import com.carigo.vehicle.helper.KycStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleDto {

    private String vehicleId;

    private String userId;

    private String vehicleNumber;

    private String vehicleType;

    private String manufacturer;

    private String model;

    private Integer manufactureYear;

    private String fuelType;

    private String color;

    private String ownerName;

    private String ownerAddress;

    private String chassisNumber;

    private String engineNumber;

    private String rcFrontImageUrl;

    private String rcBackImageUrl;

    private KycStatus kycStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
