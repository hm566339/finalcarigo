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
public class OwnerSummary implements Serializable {

    private Long ownerId;
    private String name;
    private double rating;
    private String kycStatus;
    private boolean blocked;
}
