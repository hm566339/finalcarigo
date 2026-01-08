package com.carigo.booking.service;

import com.carigo.booking.dto.BookingTrendDTO;
import com.carigo.booking.helper.BookingStatus;
import com.carigo.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBookingService {

    private final BookingRepository bookingRepository;

    // ===== DASHBOARD COUNTS =====

    public long countAllBookings() {
        return bookingRepository.count();
    }

    public long countTodayBookings() {
        return bookingRepository.countByCreatedAtBetween(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay());
    }

    public Long countUpcomingBookings() {
        return bookingRepository.countUpcomingBookings(
                LocalDateTime.now(),
                List.of(BookingStatus.APPROVED, BookingStatus.PAID));
    }

    public long countOngoingBookings() {
        return bookingRepository.countByStatus(BookingStatus.ONGOING);
    }

    public long countCompletedBookings() {
        return bookingRepository.countByStatus(BookingStatus.COMPLETED);
    }

    public long countCancelledBookings() {
        return bookingRepository.countByStatus(BookingStatus.CANCELLED);
    }

    public Long countPaymentPendingBookings() {
        return bookingRepository.countByStatus(BookingStatus.APPROVED);
    }

    public Long countDisputedBookings() {
        return bookingRepository.countStaleDisputes(LocalDateTime.now());
    }

    public Long countStaleDisputes() {
        return bookingRepository.countStaleDisputes(
                LocalDateTime.now().minusDays(7));
    }

    public List<BookingTrendDTO> getLast7DaysTrends() {

        LocalDateTime from = LocalDate.now()
                .minusDays(6)
                .atStartOfDay();

        return bookingRepository.countBookingsTrend(from)
                .stream()
                .map(row -> new BookingTrendDTO(
                        row[0].toString(),
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).longValue(),
                        ((Number) row[3]).longValue()))
                .toList();
    }

}
