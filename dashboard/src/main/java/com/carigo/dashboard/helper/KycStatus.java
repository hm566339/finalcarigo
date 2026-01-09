package com.carigo.dashboard.helper;

public enum KycStatus {

    PENDING, // Documents uploaded, verification pending
    VERIFIED, // KYC approved
    REJECTED, // KYC rejected
    EXPIRED, // Document expired (DL / RC / Insurance)
    SUSPENDED // Temporarily blocked by admin
}
