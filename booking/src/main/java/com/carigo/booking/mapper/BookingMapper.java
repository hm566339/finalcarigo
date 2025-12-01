package com.carigo.booking.mapper;

import com.carigo.booking.dto.BookingRequestDTO;
import com.carigo.booking.dto.BookingResponseDTO;
import com.carigo.booking.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    // =================== RequestDTO → Entity ===================
    public Booking toEntity(BookingRequestDTO dto) {
        if (dto == null)
            return null;

        Booking booking = new Booking();

        booking.setRenterId(dto.getRenterId());
        booking.setOwnerId(dto.getOwnerId());
        booking.setVehicleId(dto.getVehicleId());

        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());

        booking.setPrice(dto.getPrice());
        booking.setSecurityDeposit(dto.getSecurityDeposit());

        // Status set नहीं करते — service खुद status सेट करती है (PENDING)
        return booking;
    }

    // =================== Entity → ResponseDTO ==================
    public BookingResponseDTO toDTO(Booking booking) {
        if (booking == null)
            return null;

        BookingResponseDTO dto = new BookingResponseDTO();

        dto.setId(booking.getId());
        dto.setRenterId(booking.getRenterId());
        dto.setOwnerId(booking.getOwnerId());
        dto.setVehicleId(booking.getVehicleId());

        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());

        dto.setPrice(booking.getPrice());
        dto.setSecurityDeposit(booking.getSecurityDeposit());

        dto.setStatus(booking.getStatus());

        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());

        return dto;
    }
}
