package com.carigo.drivinglicense.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dl_kyc_history", indexes = {
        @Index(name = "idx_drivinglicense_kyc", columnList = "drivinglicense_id")
})
public class DlKycHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign-key reference to the drivinglicense
    @Column(name = "drivinglicense_id", nullable = false, length = 50)
    private String dlId;

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
