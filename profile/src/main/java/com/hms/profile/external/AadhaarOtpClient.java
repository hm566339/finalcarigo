package com.hms.profile.external;

import java.util.Map;

public interface AadhaarOtpClient {
    public Map<String, Object> verifyOtp(String txnId, String otp);

    public Map<String, Object> generateOtp(String aadhaar);

}
