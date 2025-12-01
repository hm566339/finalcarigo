package com.hms.user.userms.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PasswordResetOtp {

    @Id
    private String email;

    private String otp;

    private LocalDateTime expiry;
}
