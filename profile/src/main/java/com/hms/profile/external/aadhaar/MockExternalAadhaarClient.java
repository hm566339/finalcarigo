package com.hms.profile.external.aadhaar;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hms.profile.external.ExternalAadhaarClient;

@Component
@Profile({ "dev", "default" })
public class MockExternalAadhaarClient implements ExternalAadhaarClient {

    @Override
    public Map<String, Object> verifyAadhaar(String aadhaarNumber) {
        return Map.of(
                "status", "VALID",
                "name", "Mock User",
                "dob", "1995-05-20",
                "maskedAadhaar", "XXXX-XXXX-1234",
                "mock", true);
    }
}
