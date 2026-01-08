package com.carigo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Earnings implements Serializable {

    private double today;
    private double thisMonth;
    private double lifetime;
    private double walletBalance;
}
