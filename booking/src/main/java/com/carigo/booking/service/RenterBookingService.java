package com.carigo.booking.service;

import com.carigo.booking.client.*;
import com.carigo.booking.dto.*;
import com.carigo.booking.helper.BookingStatus;
import com.carigo.booking.mapper.BookingMapper;
import com.carigo.booking.model.Booking;
import com.carigo.booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RenterBookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserVerify userVerify;
    private final Vehicle vehicle;
    private final ReviewClient reviewClient;

    // ===== RENTER ACTIONS =====

    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        if (!userVerify.exitRenters(dto.getRenterId())) {
            throw new RuntimeException("Renter not found");
        }

        if (!vehicle.userAndVehicle(
                new UserAndVehicleVerify(
                        dto.getOwnerId(),
                        dto.getVehicleId(),
                        dto.getPrice()))) {
            throw new RuntimeException("Invalid vehicle");
        }

        Booking booking = bookingMapper.toEntity(dto);
        booking.setStatus(BookingStatus.PENDING);

        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO paymentSuccess(Long bookingId) {
        return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.PAID));
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    public void submitReview(Long bookingId, ReviewRequestDto dto) {
        Booking booking = getBooking(bookingId);
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Trip not completed");
        }
        reviewClient.submitReview(dto);
    }

    // ===== RENTER DASHBOARD =====

    public Long countTotalBookings(Long renterId) {
        return bookingRepository.countByRenterId(renterId);
    }

    public List<BookingResponseDTO> getActiveBookings(Long renterId) {
        return bookingRepository.findActiveBookingsForRenter(renterId)
                .stream().map(bookingMapper::toDTO).toList();
    }

    // ===== HELPERS =====

    private Booking updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    private Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    // ================= COMPLETED BOOKINGS =================
    public long countCompletedBookings(Long renterId) {
        return bookingRepository.countByRenterIdAndStatus(
                renterId,
                BookingStatus.COMPLETED);
    }

    // ================= CURRENT TRIP =================
    public BookingResponseDTO getCurrentTrip(Long renterId) {

        Booking booking = bookingRepository
                .findFirstByRenterIdAndStatusOrderByStartDateDesc(
                        renterId,
                        BookingStatus.ONGOING)
                .orElse(null);

        return bookingMapper.toDTO(booking);
    }
}
