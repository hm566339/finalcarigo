package com.carigo.vehicle.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.carigo.vehicle.helper.KycStatus;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicle_number", columnList = "vehicle_number"),
        @Index(name = "idx_user_vehicle", columnList = "user_id, vehicle_number")
})
public class VehicleEntity {

    @Id
    @Column(name = "vehicle_id", nullable = false, updatable = false, length = 50)
    private String vehicleId = UUID.randomUUID().toString();

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "vehicle_number", nullable = false, unique = true, length = 20)
    private String vehicleNumber; // RC Number

    @Column(length = 30)
    private String vehicleType; // Car / Bike / Scooter

    @Column(length = 50)
    private String manufacturer;

    @Column(length = 50)
    private String model;

    private Integer manufactureYear;

    @Column(length = 20)
    private String fuelType; // Petrol / Diesel / CNG / EV

    @Column(length = 30)
    private String color;

    // Owner Details
    @Column(length = 100)
    private String ownerName;

    @Column(length = 255)
    private String ownerAddress;

    // Technical Identifiers
    @Column(length = 50)
    private String chassisNumber;

    @Column(length = 50)
    private String engineNumber;

    // RC Images (store URLs)
    @Column(length = 1000)
    private String rcFrontImageUrl;

    @Column(length = 1000)
    private String rcBackImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    // audit fields
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
