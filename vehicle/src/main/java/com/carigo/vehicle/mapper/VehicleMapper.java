package com.carigo.vehicle.mapper;

import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.model.VehicleEntity;

public class VehicleMapper {

    private VehicleMapper() {
    }

    // Entity → DTO
    public static VehicleDto toDto(VehicleEntity e) {
        if (e == null)
            return null;

        VehicleDto d = new VehicleDto();
        d.setVehicleId(e.getVehicleId());
        d.setUserId(e.getUserId());
        d.setVehicleNumber(e.getVehicleNumber());
        d.setVehicleType(e.getVehicleType());
        d.setManufacturer(e.getManufacturer());
        d.setModel(e.getModel());
        d.setManufactureYear(e.getManufactureYear());
        d.setFuelType(e.getFuelType());
        d.setColor(e.getColor());
        d.setOwnerName(e.getOwnerName());
        d.setOwnerAddress(e.getOwnerAddress());
        d.setChassisNumber(e.getChassisNumber());
        d.setEngineNumber(e.getEngineNumber());
        d.setRcFrontImageUrl(e.getRcFrontImageUrl());
        d.setRcBackImageUrl(e.getRcBackImageUrl());
        d.setKycStatus(e.getKycStatus());
        d.setCreatedAt(e.getCreatedAt());
        d.setUpdatedAt(e.getUpdatedAt());
        return d;
    }

    // DTO → Entity (create)
    public static VehicleEntity toEntity(AddVehicleRequest r) {
        VehicleEntity e = new VehicleEntity();

        e.setVehicleNumber(r.getVehicleNumber());
        e.setVehicleType(r.getVehicleType());
        e.setManufacturer(r.getManufacturer());
        e.setModel(r.getModel());
        e.setManufactureYear(r.getManufactureYear());
        e.setFuelType(r.getFuelType());
        e.setColor(r.getColor());
        e.setOwnerName(r.getOwnerName());
        e.setOwnerAddress(r.getOwnerAddress());
        e.setChassisNumber(r.getChassisNumber());
        e.setEngineNumber(r.getEngineNumber());

        return e;
    }

    // DTO → Entity (update)
    public static void updateEntity(VehicleEntity e, AddVehicleRequest r) {
        if (r.getVehicleNumber() != null)
            e.setVehicleNumber(r.getVehicleNumber());
        if (r.getVehicleType() != null)
            e.setVehicleType(r.getVehicleType());
        if (r.getManufacturer() != null)
            e.setManufacturer(r.getManufacturer());
        if (r.getModel() != null)
            e.setModel(r.getModel());
        if (r.getManufactureYear() != null)
            e.setManufactureYear(r.getManufactureYear());
        if (r.getFuelType() != null)
            e.setFuelType(r.getFuelType());
        if (r.getColor() != null)
            e.setColor(r.getColor());
        if (r.getOwnerName() != null)
            e.setOwnerName(r.getOwnerName());
        if (r.getOwnerAddress() != null)
            e.setOwnerAddress(r.getOwnerAddress());
        if (r.getChassisNumber() != null)
            e.setChassisNumber(r.getChassisNumber());
        if (r.getEngineNumber() != null)
            e.setEngineNumber(r.getEngineNumber());
    }

    // DTO → Entity (VehicleDto → VehicleEntity)
    public static VehicleEntity fromDto(VehicleDto r) {
        if (r == null)
            return null;

        VehicleEntity e = new VehicleEntity();

        e.setVehicleId(r.getVehicleId());
        e.setUserId(r.getUserId());
        e.setVehicleNumber(r.getVehicleNumber());
        e.setVehicleType(r.getVehicleType());
        e.setManufacturer(r.getManufacturer());
        e.setModel(r.getModel());
        e.setManufactureYear(r.getManufactureYear());
        e.setFuelType(r.getFuelType());
        e.setColor(r.getColor());
        e.setOwnerName(r.getOwnerName());
        e.setOwnerAddress(r.getOwnerAddress());
        e.setChassisNumber(r.getChassisNumber());
        e.setEngineNumber(r.getEngineNumber());
        e.setRcFrontImageUrl(r.getRcFrontImageUrl());
        e.setRcBackImageUrl(r.getRcBackImageUrl());
        e.setKycStatus(r.getKycStatus());

        return e;
    }

}
