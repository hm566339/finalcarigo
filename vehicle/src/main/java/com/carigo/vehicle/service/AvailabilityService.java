package com.carigo.vehicle.service;

import com.carigo.vehicle.dto.AvailabilityRequest;
import com.carigo.vehicle.dto.AvailabilitySlotDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityService {

    List<AvailabilitySlotDto> getAvailability(String vehicleId, LocalDateTime from, LocalDateTime to);

    AvailabilitySlotDto addSlot(AvailabilityRequest request);

    void clearAvailability(String vehicleId); // optional helper
}
