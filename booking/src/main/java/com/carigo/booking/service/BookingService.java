package com.carigo.booking.service;

import com.carigo.booking.client.UserVerify;
import com.carigo.booking.client.Vehicle;
import com.carigo.booking.dto.BookingRequestDTO;
import com.carigo.booking.dto.BookingResponseDTO;
import com.carigo.booking.dto.UserAndVehicleVerify;
import com.carigo.booking.helper.BookingStatus;
import com.carigo.booking.mapper.BookingMapper;
import com.carigo.booking.model.Booking;
import com.carigo.booking.repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserVerify userVerify;
    private final Vehicle vehicle;

    // =========================== CREATE BOOKING =================================
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        // DTO → Entity
        Booking booking = bookingMapper.toEntity(dto);

        // 1) Verify renter exists
        boolean renterExists = userVerify.exitRenters(dto.getRenterId());
        if (!renterExists) {
            throw new RuntimeException("Renter does not exist");
        }

        // 2) Verify owner + vehicle ID
        UserAndVehicleVerify verify = new UserAndVehicleVerify();
        verify.setOwnerId(dto.getOwnerId());
        verify.setVehicleId(dto.getVehicleId());

        boolean isValid = vehicle.userAndVehicle(verify);
        if (!isValid) {
            throw new RuntimeException("Vehicle does not belong to this owner");
        }

        // 3) Availability check
        if (!checkAvailability(booking.getVehicleId(), booking.getStartDate(), booking.getEndDate())) {
            throw new RuntimeException("Vehicle is already booked in selected dates.");
        }

        // 4) Create booking
        booking.setStatus(BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);

        // Entity → DTO
        return bookingMapper.toDTO(saved);
    }

    // =========================== CHECK AVAILABILITY ==============================
    public boolean checkAvailability(String vehicleId, LocalDateTime start, LocalDateTime end) {
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(vehicleId, start, end);
        return overlaps.isEmpty();
    }

    // ====================== APPROVE BOOKING BY OWNER =============================
    public BookingResponseDTO approveBooking(Long bookingId) {
        Booking booking = updateStatus(bookingId, BookingStatus.APPROVED);
        return bookingMapper.toDTO(booking);
    }

    // ====================== REJECT BOOKING BY OWNER ==============================
    public BookingResponseDTO rejectBooking(Long bookingId) {
        Booking booking = updateStatus(bookingId, BookingStatus.REJECTED);
        return bookingMapper.toDTO(booking);
    }

    // ========================== PAYMENT SUCCESS ==================================
    public BookingResponseDTO paymentSuccess(Long bookingId) {
        Booking booking = updateStatus(bookingId, BookingStatus.PAID);
        return bookingMapper.toDTO(booking);
    }

    // ============================ TRIP START =====================================
    public BookingResponseDTO startTrip(Long bookingId) {
        Booking booking = updateStatus(bookingId, BookingStatus.ONGOING);
        return bookingMapper.toDTO(booking);
    }

    // ============================ TRIP END =======================================
    public BookingResponseDTO endTrip(Long bookingId) {
        Booking booking = updateStatus(bookingId, BookingStatus.COMPLETED);
        return bookingMapper.toDTO(booking);
    }

    // ============================ CANCEL BOOKING =================================
    public BookingResponseDTO cancelBooking(Long bookingId) {
        Booking booking = updateStatus(bookingId, BookingStatus.CANCELLED);
        return bookingMapper.toDTO(booking);
    }

    // ========================== INTERNAL STATUS UPDATER ==========================
    private Booking updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    // =========================== FIND BOOKING BY ID ==============================
    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}
