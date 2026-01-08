package com.carigo.vehicle.service.imp;

import com.carigo.vehicle.dto.AvailabilityRequest;
import com.carigo.vehicle.dto.AvailabilitySlotDto;
import com.carigo.vehicle.exception.ResourceNotFoundException;
import com.carigo.vehicle.model.VehicleAvailabilitySlot;
import com.carigo.vehicle.model.VehicleEntity;
import com.carigo.vehicle.repository.VehicleAvailabilityRepository;
import com.carigo.vehicle.repository.VehicleRepository;
import com.carigo.vehicle.service.AvailabilityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final VehicleAvailabilityRepository availabilityRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public List<AvailabilitySlotDto> getAvailability(String vehicleId, LocalDateTime from, LocalDateTime to) {
        List<VehicleAvailabilitySlot> slots = availabilityRepository
                .findByVehicleIdAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
                        vehicleId, from, to);

        return slots.stream().map(this::toDto).toList();
    }

    @Override
    public AvailabilitySlotDto addSlot(AvailabilityRequest request) {

        VehicleEntity vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        VehicleAvailabilitySlot slot = new VehicleAvailabilitySlot();
        slot.setVehicleId(vehicle.getVehicleId());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setAvailable(request.isAvailable());

        return toDto(availabilityRepository.save(slot));
    }

    @Override
    public void clearAvailability(String vehicleId) {
        availabilityRepository.deleteByVehicleId(vehicleId);
    }

    private AvailabilitySlotDto toDto(VehicleAvailabilitySlot s) {
        AvailabilitySlotDto d = new AvailabilitySlotDto();
        d.setId(s.getId());
        d.setVehicleId(s.getVehicleId());
        d.setStartTime(s.getStartTime());
        d.setEndTime(s.getEndTime());
        d.setAvailable(s.isAvailable());
        return d;
    }
}
