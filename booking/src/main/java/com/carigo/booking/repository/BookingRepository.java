package com.carigo.booking.repository;

import com.carigo.booking.model.Booking;
import com.carigo.booking.helper.BookingStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  // Only APPROVED + ONGOING bookings block availability
  @Query("""
          SELECT b FROM Booking b
          WHERE b.vehicleId = :vehicleId
            AND b.status IN ('APPROVED', 'ONGOING')
            AND (b.startDate < :end AND b.endDate > :start)
      """)
  List<Booking> findActiveOverlaps(
      @Param("vehicleId") String vehicleId,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  // Auto-reject all PENDING overlapping bookings when one is approved
  @Transactional
  @Modifying
  @Query("""
          UPDATE Booking b
          SET b.status = 'REJECTED'
          WHERE b.vehicleId = :vehicleId
            AND b.status = 'PENDING'
            AND b.id <> :approvedId
            AND (b.startDate < :end AND b.endDate > :start)
      """)
  void rejectOverlappingPendingBookings(
      @Param("vehicleId") String vehicleId,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end,
      @Param("approvedId") Long approvedId);

  List<Booking> findByOwnerIdAndStatus(Long ownerId, BookingStatus status);

  List<Booking> findByRenterIdOrderByCreatedAtDesc(Long renterId);

  @Query("""
          SELECT b FROM Booking b
          WHERE b.renterId = :renterId
            AND b.status IN ('PENDING','APPROVED','PAID','ONGOING')
          ORDER BY b.createdAt DESC
      """)
  List<Booking> findActiveBookingsForRenter(@Param("renterId") Long renterId);

  List<Booking> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);

  List<Booking> findByOwnerIdAndStatusOrderByCreatedAtDesc(
      Long ownerId,
      BookingStatus status);

  Page<Booking> findAll(Pageable pageable);

  List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);

  List<Booking> findByVehicleIdOrderByStartDateDesc(String vehicleId);

  long countByOwnerId(Long ownerId);

  long countByRenterId(Long renterId);

  boolean existsByIdAndOwnerId(Long bookingId, Long ownerId);

  boolean existsByIdAndRenterId(Long bookingId, Long renterId);

  // ================= UPCOMING BOOKINGS =================
  @Query("""
          SELECT COUNT(b)
          FROM Booking b
          WHERE b.startDate > :now
          AND b.status IN (:statuses)
      """)
  Long countUpcomingBookings(LocalDateTime now, Iterable<BookingStatus> statuses);

  // ================= PAYMENT PENDING =================
  Long countByStatus(BookingStatus status);

  // ================= STALE DISPUTES =================
  @Query("""
          SELECT COUNT(b)
          FROM Booking b
          WHERE b.status = 'COMPLETED'
          AND b.updatedAt < :threshold
          AND b.id NOT IN (
              SELECT r.bookingId FROM Review r
          )
      """)
  Long countStaleDisputes(LocalDateTime threshold);

  Long countByStatusAndUpdatedAtBefore(
      BookingStatus status,
      LocalDateTime time);

  long count();

  long countByCreatedAtBetween(LocalDateTime atStartOfDay, LocalDateTime atStartOfDay2);

  @Query("""
          SELECT DATE(b.createdAt),
                 SUM(CASE WHEN b.status = 'COMPLETED' THEN 1 ELSE 0 END),
                 SUM(CASE WHEN b.status = 'CANCELLED' THEN 1 ELSE 0 END),
                 COUNT(b)
          FROM Booking b
          WHERE b.createdAt >= :from
          GROUP BY DATE(b.createdAt)
          ORDER BY DATE(b.createdAt)
      """)
  List<Object[]> countBookingsTrend(LocalDateTime from);

  long countByOwnerIdAndStatus(Long ownerId, BookingStatus ongoing);

  Stream<Booking> findFirstByOwnerIdAndStatus(Long ownerId, BookingStatus ongoing);

  @Query("""
          SELECT b FROM Booking b
          WHERE b.ownerId = :ownerId
            AND b.status = 'ONGOING'
      """)
  Optional<Booking> findOngoingBooking(@Param("ownerId") Long ownerId);

  @Query("""
          SELECT COUNT(b)
          FROM Booking b
          WHERE b.ownerId = :ownerId
            AND b.status = 'COMPLETED'
            AND b.id NOT IN (
                SELECT r.bookingId FROM Review r
            )
      """)
  long countOwnerDisputes(@Param("ownerId") Long ownerId);

  long countByRenterIdAndStatus(Long renterId, BookingStatus status);

  // ✅ current ongoing trip
  Optional<Booking> findFirstByRenterIdAndStatusOrderByStartDateDesc(
      Long renterId,
      BookingStatus status);

}
