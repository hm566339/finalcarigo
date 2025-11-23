package com.hms.profile.external;

import java.util.Map;

public interface AadhaarEkycClient {
    public Map<String, Object> fetchEkyc(String txnId);
}
