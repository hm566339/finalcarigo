package com.carigo.dashboard.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerAlerts implements Serializable {

    private long insuranceExpiring;
    private long vehicleKycPending;
    private long payoutPending;
    private long disputes;
}
