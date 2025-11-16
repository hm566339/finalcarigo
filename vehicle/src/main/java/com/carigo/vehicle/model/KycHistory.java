package com.carigo.vehicle.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "kyc_history", indexes = {
        @Index(name = "idx_vehicle_kyc", columnList = "vehicle_id")
})
public class KycHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign-key reference to the vehicle
    @Column(name = "vehicle_id", nullable = false, length = 50)
    private String vehicleId;

    @Column(length = 30)
    private String action; // VERIFIED / FAILED / MANUAL_CHECK / REQUESTED

    @Column(length = 500)
    private String detail; // mismatch reason / success reason

    private LocalDateTime createdAt = LocalDateTime.now();

    // Optional: mapping (if you want)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    // private VehicleEntity vehicle;
}
