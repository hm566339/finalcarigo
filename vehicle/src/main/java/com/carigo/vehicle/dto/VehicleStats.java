package com.carigo.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleStats implements Serializable {

    private long total;
    private long active;
    private long pendingKyc;
    private long blocked;
    private long inactive;
}
