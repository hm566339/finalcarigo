package com.carigo.dashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycStats implements Serializable {

    private long ownerPending;
    private long renterPending;
    private long vehiclePending;
}
