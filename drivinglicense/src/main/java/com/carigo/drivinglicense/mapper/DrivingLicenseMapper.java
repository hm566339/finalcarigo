package com.carigo.drivinglicense.mapper;

import com.carigo.drivinglicense.dto.AddDrivingLicenseRequest;
import com.carigo.drivinglicense.dto.DrivingLicenseDto;
import com.carigo.drivinglicense.model.DrivingLicenseEntity;

public class DrivingLicenseMapper {

    public static DrivingLicenseDto toDto(DrivingLicenseEntity e) {
        DrivingLicenseDto d = new DrivingLicenseDto();

        d.setDlId(e.getDlId());
        d.setUserId(e.getUserId());
        d.setDlNumber(e.getDlNumber());
        d.setFullName(e.getFullName());
        d.setFatherName(e.getFatherName());
        d.setDateOfBirth(e.getDateOfBirth());
        d.setBloodGroup(e.getBloodGroup());
        d.setAddress(e.getAddress());
        d.setIssueDate(e.getIssueDate());
        d.setExpiryDate(e.getExpiryDate());
        d.setVehicleClasses(e.getVehicleClasses());
        d.setFrontImageUrl(e.getFrontImageUrl());
        d.setBackImageUrl(e.getBackImageUrl());
        d.setKycStatus(e.getKycStatus());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());

        return d;
    }

    public static DrivingLicenseEntity toEntity(AddDrivingLicenseRequest r) {
        DrivingLicenseEntity e = new DrivingLicenseEntity();

        e.setDlNumber(r.getDlNumber());
        e.setFullName(r.getFullName());
        e.setFatherName(r.getFatherName());
        e.setDateOfBirth(r.getDateOfBirth());
        e.setBloodGroup(r.getBloodGroup());
        e.setAddress(r.getAddress());
        e.setIssueDate(r.getIssueDate());
        e.setExpiryDate(r.getExpiryDate());
        e.setVehicleClasses(r.getVehicleClasses());

        return e;
    }
}
