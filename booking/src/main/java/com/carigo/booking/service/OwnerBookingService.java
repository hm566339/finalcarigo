package com.carigo.booking.service;

import com.carigo.booking.dto.BookingResponseDTO;
import com.carigo.booking.entity.BookingStats;
import com.carigo.booking.entity.CurrentTrip;
import com.carigo.booking.helper.BookingStatus;
import com.carigo.booking.mapper.BookingMapper;
import com.carigo.booking.model.Booking;
import com.carigo.booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerBookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    // ===== OWNER ACTIONS =====

    @Transactional
    public BookingResponseDTO approveBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(BookingStatus.APPROVED);
        return bookingMapper.toDTO(bookingRepository.save(booking));
    }

    public BookingResponseDTO rejectBooking(Long bookingId) {
        return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.REJECTED));
    }

    public BookingResponseDTO startTrip(Long bookingId) {
        return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.ONGOING));
    }

    public BookingResponseDTO endTrip(Long bookingId) {
        return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.COMPLETED));
    }

    // ===== OWNER DASHBOARD =====

    public Long countOwnerBookings(Long ownerId) {
        return bookingRepository.countByOwnerId(ownerId);
    }

    public List<BookingResponseDTO> getOngoingBookings(Long ownerId) {
        return bookingRepository
                .findByOwnerIdAndStatusOrderByCreatedAtDesc(
                        ownerId, BookingStatus.ONGOING)
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

    public BookingStats getBookingStats(Long ownerId) {
        long total = bookingRepository.countByOwnerId(ownerId);
        long ongoing = bookingRepository.countByOwnerIdAndStatus(ownerId, BookingStatus.ONGOING);
        long completed = bookingRepository.countByOwnerIdAndStatus(ownerId, BookingStatus.COMPLETED);

        return BookingStats.builder()
                .total(total)
                .ongoing(ongoing)
                .completed(completed)
                .build();
    }

    public CurrentTrip getCurrentTrip(Long ownerId) {

        return bookingRepository
                .findOngoingBooking(ownerId) // ✅ Optional<Booking>
                .map(CurrentTrip::fromBooking) // ✅ mapping safe
                .orElse(null); // ✅ no error
    }

    public long countDisputes(Long ownerId) {
        return bookingRepository.countOwnerDisputes(ownerId);
    }

}
