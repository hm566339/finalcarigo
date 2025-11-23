package com.hms.profile.external.aadhaar;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hms.profile.external.AadhaarEkycClient;

@Component
@Profile({ "dev", "default" })
public class MockAadhaarEkycClient implements AadhaarEkycClient {

    @Override
    public Map<String, Object> fetchEkyc(String txnId) {
        return Map.of(
                "status", "VALID",
                "name", "Mock User",
                "dob", "1995-05-20",
                "gender", "M",
                "maskedAadhaar", "XXXX-XXXX-1234",
                "mock", true);
    }
}
