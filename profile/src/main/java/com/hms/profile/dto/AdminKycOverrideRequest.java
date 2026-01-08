package com.hms.profile.dto;

import com.hms.profile.helper.KycStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminKycOverrideRequest {

    @NotNull
    private Long profileId;

    @NotBlank
    private String profileType; // OWNER / RENTER

    @NotNull
    private KycStatus status; // VERIFIED / FAILED / MANUAL_CHECK

    @NotBlank
    private String reason;
}
