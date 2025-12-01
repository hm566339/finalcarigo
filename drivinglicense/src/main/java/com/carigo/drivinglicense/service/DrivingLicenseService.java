package com.carigo.drivinglicense.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.carigo.drivinglicense.dto.AddDrivingLicenseRequest;
import com.carigo.drivinglicense.dto.DrivingLicenseDto;

public interface DrivingLicenseService {

    public DrivingLicenseDto add(Long id, AddDrivingLicenseRequest req) throws IOException;

    DrivingLicenseDto update(String id, AddDrivingLicenseRequest req) throws IOException;

    DrivingLicenseDto getById(String id);

    DrivingLicenseDto getByDlNumber(String dlNumber);

    List<DrivingLicenseDto> list(int page, int size);

    void delete(String id);

    DrivingLicenseDto verify(String id, Map<String, Object> externalData);

    DrivingLicenseDto getByUserId(Long id);
}
