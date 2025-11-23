package com.carigo.vehicle.service;

import java.io.IOException;
import java.util.List;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.model.VehicleEntity;

/**
 * Vehicle Service Interface
 * Production-grade interface for Vehicle KYC lifecycle.
 */
public interface VehicleService {

    VehicleDto addVehicle(Long id, AddVehicleRequest request) throws IOException;

    VehicleDto getVehicleById(String id);

    VehicleDto getByVehicleNumber(String vehicleNumber);

    VehicleDto verifyVehicle(String id, Object externalApiResponse);

    VehicleDto updateVehicle(String id, AddVehicleRequest request) throws IOException;

    void deleteVehicle(String id);

    List<VehicleDto> listAllVehicles(int page, int size);

    String insertUserId(Long userId);

    List<VehicleEntity> getVehiclesByUserId(Long userId);

}