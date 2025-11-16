package com.carigo.vehicle.service;

import java.io.IOException;
import java.util.List;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.VehicleDto;

/**
 * Vehicle Service Interface
 * Production-grade interface for Vehicle KYC lifecycle.
 */
public interface VehicleService {

    VehicleDto addVehicle(AddVehicleRequest request) throws IOException;

    VehicleDto getVehicleById(String id);

    VehicleDto getByVehicleNumber(String vehicleNumber);

    VehicleDto verifyVehicle(String id, Object externalApiResponse);

    VehicleDto updateVehicle(String id, AddVehicleRequest request) throws IOException;

    void deleteVehicle(String id);

    List<VehicleDto> listAllVehicles(int page, int size);
}