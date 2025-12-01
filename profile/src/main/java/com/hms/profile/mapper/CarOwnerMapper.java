package com.hms.profile.mapper;

import com.hms.profile.dto.CarOwnerRequestDTO;
import com.hms.profile.dto.CarOwnerResponseDTO;
import com.hms.profile.model.CarOwner;

public class CarOwnerMapper {

    public static CarOwner toEntity(CarOwnerRequestDTO dto) {
        CarOwner owner = new CarOwner();

        owner.setName(dto.getName());
        // owner.setUserId(dto.getUserId());
        // owner.setVehicleNumber(dto.getVehicleNumber());
        owner.setEmail(dto.getEmail());
        owner.setDob(dto.getDob());
        owner.setPhone(dto.getPhone());
        owner.setAddress(dto.getAddress());
        owner.setAadhaarNumber(dto.getAadhaarNumber());
        owner.setDrivingLicenseNumber(dto.getDrivingLicenseNumber());

        if (dto.getBloodGroup() != null) {
            owner.setBloodGroup(Enum.valueOf(
                    com.hms.profile.dto.BloodGroup.class,
                    dto.getBloodGroup().toUpperCase()));
        }

        return owner;
    }

    public static void updateEntity(CarOwner owner, CarOwnerRequestDTO dto) {

        if (dto.getName() != null)
            owner.setName(dto.getName());
        // if (dto.getVehicleNumber() != null)
        // owner.setVehicleNumber(dto.getVehicleNumber());
        if (dto.getEmail() != null)
            owner.setEmail(dto.getEmail());
        if (dto.getDob() != null)
            owner.setDob(dto.getDob());
        if (dto.getPhone() != null)
            owner.setPhone(dto.getPhone());
        if (dto.getAddress() != null)
            owner.setAddress(dto.getAddress());
        if (dto.getAadhaarNumber() != null)
            owner.setAadhaarNumber(dto.getAadhaarNumber());
        if (dto.getDrivingLicenseNumber() != null)
            owner.setDrivingLicenseNumber(dto.getDrivingLicenseNumber());

        if (dto.getBloodGroup() != null) {
            owner.setBloodGroup(Enum.valueOf(
                    com.hms.profile.dto.BloodGroup.class,
                    dto.getBloodGroup().toUpperCase()));
        }
    }

    public static CarOwnerResponseDTO toDto(CarOwner o) {
        CarOwnerResponseDTO dto = new CarOwnerResponseDTO();

        dto.setId(o.getId());
        // dto.setUserId(o.getUserId());
        // dto.setVehicleNumber(o.getVehicleNumber());
        dto.setName(o.getName());
        dto.setEmail(o.getEmail());
        dto.setDob(o.getDob());
        dto.setPhone(o.getPhone());
        dto.setAddress(o.getAddress());
        dto.setAadhaarNumber(o.getAadhaarNumber());
        dto.setDrivingLicenseNumber(o.getDrivingLicenseNumber());

        dto.setBloodGroup(o.getBloodGroup() != null ? o.getBloodGroup().name() : null);

        dto.setAadhaarFrontUrl(o.getAadhaarFrontUrl());
        dto.setAadhaarBackUrl(o.getAadhaarBackUrl());
        dto.setSelfieUrl(o.getSelfieUrl());

        dto.setWalletBalance(o.getWalletBalance());
        dto.setTotalEarnings(o.getTotalEarnings());

        dto.setBankAccountNumber(o.getBankAccountNumber());
        dto.setIfscCode(o.getIfscCode());
        dto.setAccountHolderName(o.getAccountHolderName());

        dto.setRating(o.getRating());
        dto.setTotalTripsCompleted(o.getTotalTripsCompleted());
        dto.setKycStatus(o.getKycStatus());

        dto.setCreatedAt(o.getCreatedAt());
        dto.setUpdatedAt(o.getUpdatedAt());

        return dto;
    }
}
