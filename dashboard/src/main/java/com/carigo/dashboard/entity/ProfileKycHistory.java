package com.carigo.dashboard.entity;

import lombok.Data;

import java.time.LocalDateTime;

import com.carigo.dashboard.helper.KycStatus;

@Data
public class ProfileKycHistory {

    private Long id;

    private Long profileId;

    private String profileType; // OWNER / RENTER

    private KycStatus action; // PENDING / VERIFIED / FAILED / MANUAL_CHECK

    private String detail;

    private LocalDateTime createdAt;

    public Enum<BookingStatus> getStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

}
