package com.hms.profile.mapper;

import com.hms.profile.dto.RenterRequestDTO;
import com.hms.profile.dto.RenterResponseDTO;
import com.hms.profile.model.Renter;

public class RenterMapper {

    public static Renter toEntity(RenterRequestDTO dto) {
        Renter r = new Renter();

        // r.setEmail(dto.getEmail());
        r.setDob(dto.getDob());
        r.setPhone(dto.getPhone());
        r.setAddress(dto.getAddress());
        r.setAadhaarNumber(dto.getAadhaarNumber());
        r.setDrivingLicenseNumber(dto.getDrivingLicenseNumber());

        return r;
    }

    public static void updateEntity(Renter r, RenterRequestDTO dto) {

        // if (dto.getEmail() != null)
        // r.setEmail(dto.getEmail());
        if (dto.getDob() != null)
            r.setDob(dto.getDob());
        if (dto.getPhone() != null)
            r.setPhone(dto.getPhone());
        if (dto.getAddress() != null)
            r.setAddress(dto.getAddress());
        if (dto.getAadhaarNumber() != null)
            r.setAadhaarNumber(dto.getAadhaarNumber());
        if (dto.getDrivingLicenseNumber() != null)
            r.setDrivingLicenseNumber(dto.getDrivingLicenseNumber());
    }

    public static RenterResponseDTO toDto(Renter r) {
        RenterResponseDTO dto = new RenterResponseDTO();

        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setEmail(r.getEmail());
        dto.setDob(r.getDob());
        dto.setPhone(r.getPhone());
        dto.setAddress(r.getAddress());

        dto.setAadhaarNumber(r.getAadhaarNumber());
        dto.setDrivingLicenseNumber(r.getDrivingLicenseNumber());

        dto.setAadhaarFrontUrl(r.getAadhaarFrontUrl());
        dto.setAadhaarBackUrl(r.getAadhaarBackUrl());
        dto.setSelfieUrl(r.getSelfieUrl());

        dto.setRating(r.getRating());
        dto.setTotalTrips(r.getTotalTrips());

        dto.setKycStatus(r.getKycStatus());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());

        return dto;
    }
}
