package com.hms.profile.external;

import java.util.Map;

public interface AadhaarFaceClient {
    public Map<String, Object> matchFace(String aadhaarPhoto, String selfie);

}
