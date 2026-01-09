package com.carigo.dashboard.entity;

import com.carigo.dashboard.helper.KycStatus;

import lombok.Data;

@Data
public class CarOwnerResponseDTO {

    // 🔑 Identity
    private Long id;

    // 👤 Basic Info
    private String name;
    private String email;
    private String phone;

    // ⭐ Trust
    private Double rating;

    // 🔐 KYC
    private KycStatus kycStatus; // PENDING / VERIFIED / REJECTED

    // 🚫 Account State
    private boolean blocked;
}
