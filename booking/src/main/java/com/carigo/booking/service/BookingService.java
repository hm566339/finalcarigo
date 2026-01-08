// // package com.carigo.booking.service;

// // import com.carigo.booking.client.UserVerify;
// // import com.carigo.booking.client.Vehicle;
// // import com.carigo.booking.client.NotificationClient;
// // import com.carigo.booking.client.ReviewClient;
// // import com.carigo.booking.dto.*;
// // import com.carigo.booking.helper.BookingStatus;
// // import com.carigo.booking.helper.ChannelType;
// // import com.carigo.booking.helper.NotificationEventType;
// // import com.carigo.booking.mapper.BookingMapper;
// // import com.carigo.booking.model.Booking;
// // import com.carigo.booking.repository.BookingRepository;

// // import jakarta.transaction.Transactional;
// // import lombok.RequiredArgsConstructor;

// // import org.springframework.data.domain.PageRequest;
// // import org.springframework.stereotype.Service;

// // import java.time.Duration;
// // import java.time.LocalDateTime;
// // import java.util.List;

// // @Service
// // @RequiredArgsConstructor
// // public class BookingService {

// // private final BookingRepository bookingRepository;
// // private final BookingMapper bookingMapper;
// // private final UserVerify userVerify;
// // private final Vehicle vehicle;
// // private final NotificationClient notificationClient;
// // private final ReviewClient reviewClient;

// // // ============================
// // // FETCH EMAIL HELPERS
// // // ============================
// // private String getOwnerEmail(Long ownerId) {
// // var owner = userVerify.getOwner(ownerId);
// // return owner.getEmail();
// // }

// // private String getRenterEmail(Long renterId) {
// // var renter = userVerify.getRenter(renterId);
// // return renter.getEmail();
// // }

// // // ============================
// // // SEND NOTIFICATION (FINAL)
// // // ============================
// // private void sendNotification(Long userId,
// // NotificationEventType event,
// // String title,
// // String message) {

// // NotificationRequest req = new NotificationRequest();
// // req.setUserId(userId);
// // req.setEventType(event);
// // req.setChannel(ChannelType.EMAIL); // Only email
// // req.setTitle(title);
// // req.setMessage(message);

// // // ⭐ Intelligent Email Routing
// // if (event == NotificationEventType.BOOKING_REQUEST) {
// // req.setEmail(getOwnerEmail(userId)); // Owner gets booking request
// // } else {
// // req.setEmail(getRenterEmail(userId)); // Renter gets
// approval/rejection/payment/trip updates
// // }

// // notificationClient.sendNotification(req);
// // }

// // // ================= CREATE BOOKING ======================
// // public BookingResponseDTO createBooking(BookingRequestDTO dto) {

// // Booking booking = bookingMapper.toEntity(dto);

// // if (!userVerify.exitRenters(dto.getRenterId())) {
// // throw new RuntimeException("Renter does not exist");
// // }

// // UserAndVehicleVerify verify = new UserAndVehicleVerify();
// // verify.setOwnerId(dto.getOwnerId());
// // verify.setVehicleId(dto.getVehicleId());
// // verify.setPreDay(dto.getPrice());

// // if (!vehicle.userAndVehicle(verify)) {
// // throw new RuntimeException("Vehicle does not belong to this owner");
// // }

// // if (vehicle.checkMaintenance(dto.getVehicleId(), dto.getStartDate(),
// dto.getEndDate())) {
// // throw new RuntimeException("Vehicle is under maintenance.");
// // }

// // if (!checkAvailability(dto.getVehicleId(), dto.getStartDate(),
// dto.getEndDate())) {
// // throw new RuntimeException("Vehicle is already booked.");
// // }

// // booking.setStatus(BookingStatus.PENDING);
// // Booking saved = bookingRepository.save(booking);

// // // ⭐ SEND TO OWNER
// // sendNotification(
// // dto.getOwnerId(),
// // NotificationEventType.BOOKING_REQUEST,
// // "New Booking Request",
// // "You received a new booking request for vehicle: " + dto.getVehicleId());

// // return bookingMapper.toDTO(saved);
// // }

// // // ================= CHECK AVAILABILITY ======================
// // public boolean checkAvailability(String vehicleId, LocalDateTime start,
// LocalDateTime end) {

// // if (vehicle.checkMaintenance(vehicleId, start, end)) {
// // return false;
// // }

// // List<Booking> overlaps = bookingRepository.findActiveOverlaps(vehicleId,
// start, end);
// // return overlaps.isEmpty();
// // }

// // // ================= APPROVE BOOKING ======================
// // @Transactional
// // public BookingResponseDTO approveBooking(Long bookingId) {

// // Booking booking = getBooking(bookingId);

// // if (!checkAvailability(booking.getVehicleId(), booking.getStartDate(),
// booking.getEndDate())) {
// // throw new RuntimeException("Vehicle unavailable.");
// // }

// // booking.setStatus(BookingStatus.APPROVED);
// // bookingRepository.save(booking);

// // bookingRepository.rejectOverlappingPendingBookings(
// // booking.getVehicleId(),
// // booking.getStartDate(),
// // booking.getEndDate(),
// // booking.getId());

// // // ⭐ SEND EMAIL TO RENTER ONLY
// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.BOOKING_APPROVED,
// // "Booking Approved",
// // "Your booking has been approved!");

// // return bookingMapper.toDTO(booking);
// // }

// // // ================= REJECT BOOKING ======================
// // public BookingResponseDTO rejectBooking(Long bookingId) {
// // Booking booking = updateStatus(bookingId, BookingStatus.REJECTED);

// // // ⭐ email → RENTER
// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.BOOKING_REJECTED,
// // "Booking Rejected",
// // "Your booking request has been rejected.");

// // return bookingMapper.toDTO(booking);
// // }

// // // ================= PAYMENT SUCCESS ======================
// // public BookingResponseDTO paymentSuccess(Long bookingId) {
// // Booking booking = updateStatus(bookingId, BookingStatus.PAID);

// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.PAYMENT_SUCCESS,
// // "Payment Successful",
// // "Your payment has been successfully processed.");

// // return bookingMapper.toDTO(booking);
// // }

// // // ================= START TRIP ==========================
// // public BookingResponseDTO startTrip(Long bookingId) {
// // Booking booking = updateStatus(bookingId, BookingStatus.ONGOING);

// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.TRIP_STARTED,
// // "Trip Started",
// // "Your trip has officially started!");

// // return bookingMapper.toDTO(booking);
// // }

// // // ================= END TRIP ============================
// // public BookingResponseDTO endTrip(Long bookingId) {
// // Booking booking = updateStatus(bookingId, BookingStatus.COMPLETED);

// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.TRIP_COMPLETED,
// // "Trip Completed",
// // "Your trip is complete. Please leave a review!");

// // return bookingMapper.toDTO(booking);
// // }

// // // ================= CANCEL BOOKING ======================
// // @Transactional
// // public BookingResponseDTO cancelBooking(Long bookingId) {

// // Booking booking = getBooking(bookingId);

// // if (booking.getStatus() == BookingStatus.CANCELLED ||
// // booking.getStatus() == BookingStatus.COMPLETED) {
// // throw new RuntimeException("Booking cannot be cancelled.");
// // }

// // LocalDateTime now = LocalDateTime.now();
// // long hoursLeft = Duration.between(now, booking.getStartDate()).toHours();

// // String msg;

// // if (hoursLeft > 48)
// // msg = "90% refund applied.";
// // else if (hoursLeft >= 24)
// // msg = "50% refund applied.";
// // else
// // msg = "No refund within 24 hours.";

// // booking.setStatus(BookingStatus.CANCELLED);
// // bookingRepository.save(booking);

// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.BOOKING_REJECTED,
// // "Booking Cancelled",
// // msg);

// // return bookingMapper.toDTO(booking);
// // }

// // // ================= EXTEND TRIP ==========================
// // @Transactional
// // public BookingResponseDTO extendTrip(Long bookingId, TripExtensionRequest
// request) {

// // Booking booking = getBooking(bookingId);

// // if (booking.getStatus() != BookingStatus.PAID &&
// // booking.getStatus() != BookingStatus.ONGOING) {
// // throw new RuntimeException("Extension allowed only when trip is PAID or
// ONGOING.");
// // }

// // if (request.getNewEndDate() == null ||
// // !request.getNewEndDate().isAfter(booking.getEndDate())) {
// // throw new RuntimeException("Invalid new end time.");
// // }

// // if (!checkAvailability(booking.getVehicleId(), booking.getEndDate(),
// request.getNewEndDate())) {
// // throw new RuntimeException("Vehicle not available for extension.");
// // }

// // long oldHours = Duration.between(booking.getStartDate(),
// booking.getEndDate()).toHours();
// // long oldDays = Math.max(1, (long) Math.ceil(oldHours / 24.0));

// // double perDay = booking.getPrice() / oldDays;

// // long newHours = Duration.between(booking.getStartDate(),
// request.getNewEndDate()).toHours();
// // long newDays = Math.max(1, (long) Math.ceil(newHours / 24.0));

// // booking.setPrice(newDays * perDay);
// // booking.setEndDate(request.getNewEndDate());

// // Booking saved = bookingRepository.save(booking);

// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.TRIP_STARTED,
// // "Trip Extended",
// // "Your trip is extended till " + request.getNewEndDate());

// // return bookingMapper.toDTO(saved);
// // }

// // // ================= REVIEW ==========================
// // @Transactional
// // public void submitReview(Long bookingId, ReviewRequestDto dto) {

// // Booking booking = getBooking(bookingId);

// // if (booking.getStatus() != BookingStatus.COMPLETED) {
// // throw new RuntimeException("Review allowed only after trip completion.");
// // }

// // if (!booking.getRenterId().equals(dto.getRenterId())) {
// // throw new RuntimeException("Only the renter can review this trip.");
// // }

// // reviewClient.submitReview(dto);

// // sendNotification(
// // booking.getOwnerId(),
// // NotificationEventType.TRIP_COMPLETED,
// // "New Review Received",
// // "You received a new review for booking #" + bookingId);
// // }

// // // ================= HELPERS ==========================
// // private Booking updateStatus(Long bookingId, BookingStatus status) {
// // Booking booking = getBooking(bookingId);
// // booking.setStatus(status);
// // return bookingRepository.save(booking);
// // }

// // private Booking getBooking(Long id) {
// // return bookingRepository.findById(id)
// // .orElseThrow(() -> new RuntimeException("Booking not found"));
// // }

// // public List<BookingResponseDTO> getBookingRequestsForOwner(Long ownerId) {
// // return bookingRepository.findByOwnerIdAndStatus(ownerId,
// BookingStatus.PENDING)
// // .stream().map(bookingMapper::toDTO).toList();
// // }

// // public List<BookingResponseDTO> getBookingsForRenter(Long renterId) {

// // // Optional safety check
// // if (!userVerify.exitRenters(renterId)) {
// // throw new RuntimeException("Renter does not exist");
// // }

// // return bookingRepository.findByRenterIdOrderByCreatedAtDesc(renterId)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // public List<BookingResponseDTO> getActiveBookingsForRenter(Long renterId)
// {

// // // Optional safety check
// // if (!userVerify.exitRenters(renterId)) {
// // throw new RuntimeException("Renter does not exist");
// // }

// // return bookingRepository.findActiveBookingsForRenter(renterId)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // public List<BookingResponseDTO> getBookingsForOwner(Long ownerId) {

// // // Optional safety check
// // if (!userVerify.isPresent(ownerId)) {
// // throw new RuntimeException("Owner does not exist");
// // }

// // return bookingRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // // ================= OWNER ONGOING BOOKINGS =================
// // public List<BookingResponseDTO> getOngoingBookingsForOwner(Long ownerId) {

// // if (!userVerify.isPresent(ownerId)) {
// // throw new RuntimeException("Owner does not exist");
// // }

// // return bookingRepository
// // .findByOwnerIdAndStatusOrderByCreatedAtDesc(
// // ownerId, BookingStatus.ONGOING)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // // ================= OWNER COMPLETED BOOKINGS =================
// // public List<BookingResponseDTO> getCompletedBookingsForOwner(Long ownerId)
// {

// // if (!userVerify.isPresent(ownerId)) {
// // throw new RuntimeException("Owner does not exist");
// // }

// // return bookingRepository
// // .findByOwnerIdAndStatusOrderByCreatedAtDesc(
// // ownerId, BookingStatus.COMPLETED)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // public List<BookingResponseDTO> getAllBookings(int page, int size) {

// // return bookingRepository
// // .findAll(PageRequest.of(page, size))
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // public List<BookingResponseDTO> getBookingsByStatus(BookingStatus status)
// {

// // return bookingRepository
// // .findByStatusOrderByCreatedAtDesc(status)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // @Transactional
// // public BookingResponseDTO forceCancelBooking(Long bookingId) {

// // Booking booking = getBooking(bookingId);

// // if (booking.getStatus() == BookingStatus.COMPLETED ||
// // booking.getStatus() == BookingStatus.CANCELLED) {
// // throw new RuntimeException("Booking already closed");
// // }

// // booking.setStatus(BookingStatus.CANCELLED);
// // Booking saved = bookingRepository.save(booking);

// // // Notify renter
// // sendNotification(
// // booking.getRenterId(),
// // NotificationEventType.BOOKING_REJECTED,
// // "Booking Cancelled by Admin",
// // "Your booking was force-cancelled by admin");

// // return bookingMapper.toDTO(saved);
// // }

// // public List<BookingResponseDTO> getVehicleBookingHistory(String vehicleId)
// {

// // return bookingRepository
// // .findByVehicleIdOrderByStartDateDesc(vehicleId)
// // .stream()
// // .map(bookingMapper::toDTO)
// // .toList();
// // }

// // public Long countBookingsForOwner(Long ownerId) {

// // if (!userVerify.isPresent(ownerId)) {
// // throw new RuntimeException("Owner not found");
// // }

// // return bookingRepository.countByOwnerId(ownerId);
// // }

// // public Long countBookingsForRenter(Long renterId) {

// // if (!userVerify.exitRenters(renterId)) {
// // throw new RuntimeException("Renter not found");
// // }

// // return bookingRepository.countByRenterId(renterId);
// // }

// // public boolean verifyOwnerForBooking(Long bookingId, Long ownerId) {

// // return bookingRepository.existsByIdAndOwnerId(bookingId, ownerId);
// // }

// // public boolean verifyRenterForBooking(Long bookingId, Long renterId) {

// // return bookingRepository.existsByIdAndRenterId(bookingId, renterId);
// // }

// // public boolean isVehicleAvailable(String vehicleId) {

// // LocalDateTime now = LocalDateTime.now();

// // List<Booking> activeBookings = bookingRepository.findActiveOverlaps(
// // vehicleId,
// // now.minusYears(1), // past buffer
// // now.plusYears(1)); // future buffer

// // return activeBookings.isEmpty();
// // }

// // }

// package com.carigo.booking.service;

// import com.carigo.booking.client.*;
// import com.carigo.booking.dto.*;
// import com.carigo.booking.helper.*;
// import com.carigo.booking.mapper.BookingMapper;
// import com.carigo.booking.model.Booking;
// import com.carigo.booking.repository.BookingRepository;

// import jakarta.transaction.Transactional;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// public class BookingService {

// private final BookingRepository bookingRepository;
// private final BookingMapper bookingMapper;
// private final UserVerify userVerify;
// private final Vehicle vehicle;
// private final NotificationClient notificationClient;
// private final ReviewClient reviewClient;

// // =====================================================
// // EMAIL HELPERS
// // =====================================================
// private String getOwnerEmail(Long ownerId) {
// return userVerify.getOwner(ownerId).getEmail();
// }

// private String getRenterEmail(Long renterId) {
// return userVerify.getRenter(renterId).getEmail();
// }

// // =====================================================
// // NOTIFICATION HELPER
// // =====================================================
// private void sendNotification(
// Long userId,
// NotificationEventType event,
// String title,
// String message) {

// NotificationRequest req = new NotificationRequest();
// req.setUserId(userId);
// req.setEventType(event);
// req.setChannel(ChannelType.EMAIL);
// req.setTitle(title);
// req.setMessage(message);

// if (event == NotificationEventType.BOOKING_REQUEST) {
// req.setEmail(getOwnerEmail(userId));
// } else {
// req.setEmail(getRenterEmail(userId));
// }

// notificationClient.sendNotification(req);
// }

// // =====================================================
// // CREATE BOOKING
// // =====================================================
// public BookingResponseDTO createBooking(BookingRequestDTO dto) {

// if (!userVerify.exitRenters(dto.getRenterId())) {
// throw new RuntimeException("Renter does not exist");
// }

// UserAndVehicleVerify verify = new UserAndVehicleVerify();
// verify.setOwnerId(dto.getOwnerId());
// verify.setVehicleId(dto.getVehicleId());
// verify.setPreDay(dto.getPrice());

// if (!vehicle.userAndVehicle(verify)) {
// throw new RuntimeException("Vehicle does not belong to owner");
// }

// if (vehicle.checkMaintenance(dto.getVehicleId(), dto.getStartDate(),
// dto.getEndDate())) {
// throw new RuntimeException("Vehicle under maintenance");
// }

// if (!checkAvailability(dto.getVehicleId(), dto.getStartDate(),
// dto.getEndDate())) {
// throw new RuntimeException("Vehicle already booked");
// }

// Booking booking = bookingMapper.toEntity(dto);
// booking.setStatus(BookingStatus.PENDING);

// Booking saved = bookingRepository.save(booking);

// sendNotification(
// dto.getOwnerId(),
// NotificationEventType.BOOKING_REQUEST,
// "New Booking Request",
// "New booking request for vehicle: " + dto.getVehicleId());

// return bookingMapper.toDTO(saved);
// }

// // =====================================================
// // AVAILABILITY
// // =====================================================
// public boolean checkAvailability(String vehicleId, LocalDateTime start,
// LocalDateTime end) {

// if (vehicle.checkMaintenance(vehicleId, start, end)) {
// return false;
// }

// return bookingRepository
// .findActiveOverlaps(vehicleId, start, end)
// .isEmpty();
// }

// // =====================================================
// // APPROVE BOOKING
// // =====================================================
// @Transactional
// public BookingResponseDTO approveBooking(Long bookingId) {

// Booking booking = getBooking(bookingId);

// if (!checkAvailability(
// booking.getVehicleId(),
// booking.getStartDate(),
// booking.getEndDate())) {
// throw new RuntimeException("Vehicle unavailable");
// }

// booking.setStatus(BookingStatus.APPROVED);
// bookingRepository.save(booking);

// bookingRepository.rejectOverlappingPendingBookings(
// booking.getVehicleId(),
// booking.getStartDate(),
// booking.getEndDate(),
// booking.getId());

// sendNotification(
// booking.getRenterId(),
// NotificationEventType.BOOKING_APPROVED,
// "Booking Approved",
// "Your booking has been approved");

// return bookingMapper.toDTO(booking);
// }

// // =====================================================
// // STATUS UPDATES
// // =====================================================
// public BookingResponseDTO rejectBooking(Long bookingId) {
// return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.REJECTED));
// }

// public BookingResponseDTO paymentSuccess(Long bookingId) {
// return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.PAID));
// }

// public BookingResponseDTO startTrip(Long bookingId) {
// return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.ONGOING));
// }

// public BookingResponseDTO endTrip(Long bookingId) {
// return bookingMapper.toDTO(updateStatus(bookingId, BookingStatus.COMPLETED));
// }

// // =====================================================
// // CANCEL BOOKING
// // =====================================================
// @Transactional
// public BookingResponseDTO cancelBooking(Long bookingId) {

// Booking booking = getBooking(bookingId);

// if (booking.getStatus() == BookingStatus.CANCELLED ||
// booking.getStatus() == BookingStatus.COMPLETED) {
// throw new RuntimeException("Booking cannot be cancelled");
// }

// booking.setStatus(BookingStatus.CANCELLED);
// bookingRepository.save(booking);

// return bookingMapper.toDTO(booking);
// }

// // =====================================================
// // REVIEW
// // =====================================================
// @Transactional
// public void submitReview(Long bookingId, ReviewRequestDto dto) {

// Booking booking = getBooking(bookingId);

// if (booking.getStatus() != BookingStatus.COMPLETED) {
// throw new RuntimeException("Review allowed only after completion");
// }

// if (!booking.getRenterId().equals(dto.getRenterId())) {
// throw new RuntimeException("Only renter can review");
// }

// reviewClient.submitReview(dto);
// }

// // =====================================================
// // DASHBOARD / ADMIN COUNTS (FULL)
// // =====================================================
// public long countAllBookings() {
// return bookingRepository.count();
// }

// public long countTodayBookings() {
// return bookingRepository.countByCreatedAtBetween(
// LocalDate.now().atStartOfDay(),
// LocalDate.now().plusDays(1).atStartOfDay());
// }

// public Long countUpcomingBookings() {
// return bookingRepository.countUpcomingBookings(
// LocalDateTime.now(),
// List.of(BookingStatus.APPROVED, BookingStatus.PAID));
// }

// public long countOngoingBookings() {
// return bookingRepository.countByStatus(BookingStatus.ONGOING);
// }

// public long countCompletedBookings() {
// return bookingRepository.countByStatus(BookingStatus.COMPLETED);
// }

// public long countCancelledBookings() {
// return bookingRepository.countByStatus(BookingStatus.CANCELLED);
// }

// public Long countPaymentPendingBookings() {
// return bookingRepository.countByStatus(BookingStatus.APPROVED);
// }

// public Long countDisputedBookings() {
// return bookingRepository.countStaleDisputes(LocalDateTime.now());
// }

// public Long countStaleDisputes() {
// return bookingRepository.countStaleDisputes(
// LocalDateTime.now().minusDays(7));
// }

// // =====================================================
// // HELPERS
// // =====================================================
// private Booking updateStatus(Long bookingId, BookingStatus status) {
// Booking booking = getBooking(bookingId);
// booking.setStatus(status);
// return bookingRepository.save(booking);
// }

// private Booking getBooking(Long id) {
// return bookingRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Booking not found"));
// }
// }
