package com.hms.profile.external.aadhaar;

import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hms.profile.external.AadhaarFaceClient;

@Component
@Profile({ "dev", "default" })
public class MockAadhaarFaceClient implements AadhaarFaceClient {

    @Override
    public Map<String, Object> matchFace(String aadhaarPhoto, String selfie) {
        return Map.of(
                "matchScore", 98.5,
                "liveness", true,
                "status", "MATCHED",
                "mock", true);
    }
}
