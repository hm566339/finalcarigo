package com.hms.user.userms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.user.userms.model.PasswordResetOtp;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, String> {
}
