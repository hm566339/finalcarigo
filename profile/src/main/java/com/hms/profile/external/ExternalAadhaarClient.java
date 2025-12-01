package com.hms.profile.external;

import java.util.Map;

public interface ExternalAadhaarClient {
    /**
     * Queries external Aadhaar verification provider.
     * Returns a Map with keys like: status, name, dob, maskedAadhaar (or token)
     */
    Map<String, Object> verifyAadhaar(String aadhaarNumber);
}
