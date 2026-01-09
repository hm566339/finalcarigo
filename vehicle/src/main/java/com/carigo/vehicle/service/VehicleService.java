package com.carigo.vehicle.service;

import java.io.IOException;
import java.util.List;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.UserAndVehicleVerify;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.dto.VehicleStats;
import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.helper.VehicleStatus;
import com.carigo.vehicle.model.VehicleEntity;

/**
 * Vehicle Service Interface
 * Production-grade interface for Vehicle KYC lifecycle.
 */
public interface VehicleService {

    VehicleDto getVehicleById(String id);

    List<VehicleDto> getByUserId(Long id);

    VehicleDto verifyVehicle(String id, Object externalApiResponse);

    public VehicleDto updateVehicle(String id, AddVehicleRequest request) throws IOException;

    void deleteVehicle(String id);

    List<VehicleDto> listAllVehicles(int page, int size);

    void createVehicleMultipart(Long userId, AddVehicleRequest req) throws IOException;

    VehicleDto getByVehicleNumber(String number);

    Boolean exitVehicleNumber(String id);

    Boolean verifyUserAndVehicle(UserAndVehicleVerify dto);

    VehicleDto adminKycOverride(String vehicleId, KycStatus status, String reason);

    VehicleDto changeStatus(String id, VehicleStatus status);

    long countAllVehicles();

    long countActiveVehicles();

    long countBlockedVehicles();

    long countPendingKycVehicles();

    long countInsuranceExpiringSoon();

    VehicleStats getVehicleStats(Long ownerId);

    long countInsuranceExpiring(Long ownerId);

}