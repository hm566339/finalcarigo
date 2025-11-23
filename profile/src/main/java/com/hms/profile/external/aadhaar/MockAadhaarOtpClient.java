package com.hms.profile.external.aadhaar;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hms.profile.external.AadhaarOtpClient;

import java.util.Map;
import java.util.UUID;

@Component
@Profile({ "dev", "default" }) // DEV MODE MOCK
public class MockAadhaarOtpClient implements AadhaarOtpClient {

    @Override
    public Map<String, Object> generateOtp(String aadhaar) {
        return Map.of(
                "txnId", UUID.randomUUID().toString(),
                "status", "OTP_SENT",
                "mock", true);
    }

    @Override
    public Map<String, Object> verifyOtp(String txnId, String otp) {
        return Map.of(
                "status", "OTP_VERIFIED",
                "mock", true);
    }
}
