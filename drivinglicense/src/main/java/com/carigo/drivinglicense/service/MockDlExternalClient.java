package com.carigo.drivinglicense.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MockDlExternalClient implements DlExternalClient {

    @Override
    public Map<String, Object> fetchByDlNumber(String dlNumber) {

        Map<String, Object> data = new HashMap<>();

        data.put("dlNumber", dlNumber);
        data.put("fullName", "ARJUN KUMAR");
        data.put("fatherName", "RAM KUMAR");
        data.put("dob", "1997-05-06");
        data.put("issueDate", "2018-01-01");
        data.put("expiryDate", "2038-01-01");
        data.put("vehicleClasses", "MCWG, LMV");
        data.put("source", "mock-dl");

        return data;
    }
}
