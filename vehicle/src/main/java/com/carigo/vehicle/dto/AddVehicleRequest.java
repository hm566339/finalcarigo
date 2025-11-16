package com.carigo.vehicle.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

@Data
public class AddVehicleRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    private String vehicleType;

    private String manufacturer;

    private String model;

    @Min(value = 1900, message = "Manufacture year seems invalid")
    @Max(value = 2100, message = "Manufacture year seems invalid")
    private Integer manufactureYear;

    private String fuelType;

    private String color;

    private String ownerName;

    private String ownerAddress;

    private String chassisNumber;

    private String engineNumber;

    // Multipart images
    @Size(max = 5 * 1024 * 1024, message = "Front RC image too large (max 5MB)")
    private MultipartFile rcFrontImage;

    @Size(max = 5 * 1024 * 1024, message = "Back RC image too large (max 5MB)")
    private MultipartFile rcBackImage;
}
